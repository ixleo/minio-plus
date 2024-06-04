
let partMd5List = new Array();
let partCount = 0;
let partSize = 0;
let fileSize = 0;

/**
 * 注意：本测试Demo不受分片顺序影响
 * 关于上传文件成功后的处理：配置minio监听指定存储桶指定格式文件上传成功后，push通知到mq,后端程序监听并消费即可
 * （建议上传mp4，成功后可以直接在页面看到效果）
 * 测试分片上传
 *      运行页面 > 打开控制台 > console > 选择上传的文件 > 观察打印的信息
 * 测试秒传
 *      在上一个测试的基础上，刷新一下页面，选择上一次上传的文件
 * 测试断点续传
 *      重新选择一个文件(如果你没有多的测试文件，就重启一下后台服务) > 手动模拟上传了一部分失败的场景(在所有分片未上传完成时关掉页面 或 注释掉合并文件代码，然后去 minio chunk桶 删除几个分片)
 *      > 再选择刚选择的文件上传 > 观察打印的信息是否从缺失的分片开始上传
 */
uploadFile = async () => {
    //获取用户选择的文件
    const file = document.getElementById("upload").files[0];

    //获取文件md5
    let startTime = new Date();
    const fileMd5 = await getFileMd5(file);

    console.log("文件md5：", fileMd5 + "，耗时" + (new Date() - startTime)+"毫秒");

    console.log("向后端请求本次分片上传初始化")

    $.ajax({
        url: "/storage/upload/init",
        type: 'POST',
        contentType: "application/json",
        dataType: "json",
        data: JSON.stringify({
            fileMd5: fileMd5,
            fullFileName: file.name,
            fileSize: file.size,
        }),
        success: async function (res) {
            partMd5List = new Array();
            console.log("当前文件上传情况：初次上传 或 断点续传")
            document.getElementById("uploadId").value = (res.data.fileKey);
            if (res.isDone) {
                return;
            }
            const chunkUploadUrls = res.data.partList;
            partCount = res.data.partCount;
            partSize = res.data.partSize;
            fileSize = res.data.fileSize;

            //当前为顺序上传方式，若要测试并发上传，请将第52行 await 修饰符删除即可
            //若使用并发上传方式，当前分片上传完成后打印出来的完成提示是不准确的，但这并不影响最终运行结果；原因是由ajax请求本身是异步导致的

            for (const [i, item] of chunkUploadUrls.entries()) {

                //取文件指定范围内的byte，从而得到分片数据
                let _chunkFile = file.slice(item.startPosition, item.endPosition)
                console.log("开始上传第" + i + "个分片", _chunkFile)
                $.ajax({
                    url: item.url,
                    type: 'PUT',
                    contentType: false,
                    processData: false,
                    data: _chunkFile,
                    success: function (res) {
                        console.log("第" + i + "个分片上传完成")
                    }
                })
            }
        }
    })
}

calculatePartMd5 = async () => {
    //获取用户选择的文件
    const file = document.getElementById("upload").files[0];

    //获取文件md5
    let startTime = new Date();
    const fileMd5 = await getFileMd5(file);

    console.log("文件md5：", fileMd5 + "，耗时" + (new Date() - startTime)+"毫秒");

    for(let i=0;i<partCount;i++){
        console.log(i)
        let _chunkFile;
        if(i==partCount-1){
            _chunkFile = file.slice(i*partSize, fileSize)
        }else{
            _chunkFile = file.slice(i*partSize, (i+1)*partSize)
        }

        let partMd5 = await getFileMd5(_chunkFile);
        partMd5List.push(partMd5);
        console.log(partMd5List)
    }
}

function download() {
    let fileKey = document.getElementById("uploadId").value;
    window.location.href = "/storage/download/" + fileKey;
}

/**
 * 获取文件MD5
 * @param file
 * @returns {Promise<unknown>}
 */
getFileMd5 = (file) => {
    let fileReader = new FileReader()
    fileReader.readAsBinaryString(file)
    let spark = new SparkMD5()
    return new Promise((resolve) => {
        fileReader.onload = (e) => {
            spark.appendBinary(e.target.result)
            resolve(spark.end())
        }
    })
}

/**
 * 请求后端合并文件
 * @param fileMd5
 * @param fileName
 */
merge = () => {
    let fileKey = document.getElementById("uploadId").value;
    console.log("开始请求后端合并文件")
    //注意：bucketName请填写你自己的存储桶名称，如果没有，就先创建一个写在这
    $.ajax({
        url: "/storage/upload/complete/" + fileKey,
        type: 'POST',
        contentType: "application/json",
        dataType: "json",
        data: JSON.stringify({
            partMd5List:partMd5List
        }),
        success: function (res) {
            console.log("合并文件完成", res.data)
        }
    })
}


removeTaskId = async () => {
    document.getElementById("uploadId").value = '';

}