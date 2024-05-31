import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import cn.hutool.http.Method;
import com.google.common.base.Joiner;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Multimap;
import com.google.common.collect.MultimapBuilder;
import com.google.common.io.BaseEncoding;
import io.minio.Digest;


import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.time.ZonedDateTime;
import java.util.*;
import java.util.stream.Collectors;

public class MinIORestful {

    static final String serviceName = "s3";
    static final String US_EAST_1 = "us-east-1";
    private static final Set<String> IGNORED_HEADERS = ImmutableSet.of("accept-encoding", "authorization", "content-type", "content-length", "user-agent");

    static String accessKey = "minioadmin";
    static String secretKey = "minioadmin";
    static String ZERO_MD5_HASH = "1B2M2Y8AsgTpgAmY7PhCfg==";
    static String ZERO_SHA256_HASH = "e3b0c44298fc1c149afbf4c8996fb92427ae41e4649b934ca495991b7852b855";
    static String backend = "http://localhost:9000";

    public static void main(String[] args) throws NoSuchAlgorithmException, InvalidKeyException {

        // 动态传入参数
        String url = "http://localhost:9000/document";

//        ZonedDateTime date = ZonedDateTime.parse("2024-05-31T16:31:54Z");
        // 取得当前时间
        ZonedDateTime date = ZonedDateTime.now();

        HttpResponse httpResponse = request(url,"","/document","HEAD",date);

        System.out.println("httpResponse.isOk()="+httpResponse.isOk());
        System.out.println("httpResponse.getStatus()="+httpResponse.getStatus());
        System.out.println("httpResponse.headers()="+httpResponse.headers());
        System.out.println("httpResponse.body()="+httpResponse.body());

    }

    public static HttpResponse request(String url,String params,String path,String method,ZonedDateTime date) throws NoSuchAlgorithmException, InvalidKeyException {



        // 创建除Authorization外所有header
        Map<String, List<String>> headers = new HashMap<>();
        headers.put("Host", CollUtil.newArrayList(backend.replace("http://","").replace("https://","")));
        headers.put("Accept-Encoding", CollUtil.newArrayList("identity"));
        headers.put("User-Agent", CollUtil.newArrayList("MinIO (Windows 10; amd64) minio-java/8.3.3"));
        headers.put("Content-MD5", CollUtil.newArrayList(ZERO_MD5_HASH));
        headers.put("x-amz-content-sha256", CollUtil.newArrayList(ZERO_SHA256_HASH));
        headers.put("x-amz-date", CollUtil.newArrayList(date.format(Time.AMZ_DATE_FORMAT)));

        // 计算scope
        String scope =buildScope(serviceName,date);

        // 计算signedHeaders
        Map<String, String> canonicalHeaders = buildCanonicalHeaders(headers,IGNORED_HEADERS);

        // 计算signedHeaders
        String signedHeaders = buildSignedHeaders(canonicalHeaders);

        // 计算canonicalQueryString
        String canonicalQueryString = buildCanonicalQueryString(params);

        // 计算buildCanonicalRequest
        String canonicalRequestHash = buildCanonicalRequest(canonicalHeaders,signedHeaders,canonicalQueryString,method,path);

        // 计算stringToSign
        String stringToSign = buildStringToSign(date,scope,canonicalRequestHash);

        // 计算signingKey
        byte[] signingKey = buildSigningKey(serviceName,date);

        // 计算signature
        String signature = buildSignature(signingKey,stringToSign);

        // 计算authorization
        String authorization = buildAuthorization(accessKey,scope,signedHeaders,signature);

        HttpRequest httpRequest = HttpUtil.createRequest(Method.HEAD, url);
        httpRequest.header(headers,true);
        httpRequest.header("Authorization", authorization);



        System.out.println("scope="+scope);
        System.out.println("canonicalHeaders="+canonicalHeaders);
        System.out.println("signedHeaders="+signedHeaders);
        System.out.println("canonicalQueryString="+canonicalQueryString);
        System.out.println("canonicalRequestHash="+canonicalRequestHash);
        System.out.println("stringToSign="+stringToSign);
        System.out.println("signingKey="+signingKey);
        System.out.println("signature="+signature);
        System.out.println("authorization="+authorization);

        System.out.println("httpRequest.headers()="+httpRequest.headers());

        return httpRequest.execute();
    }


    // 计算scope
    public static String buildScope(String serviceName,ZonedDateTime date){
        return date.format(Time.SIGNER_DATE_FORMAT) + "/" + US_EAST_1 + "/" + serviceName + "/aws4_request";
    }

    public static Map<String, String> buildCanonicalHeaders(Map<String, List<String>> headers, Set<String> ignoredHeaders){
        Map<String, String> canonicalHeaders = new TreeMap<>();

        for (String name : headers.keySet()) {
            String signedHeader = name.toLowerCase(Locale.US);
            if (!ignoredHeaders.contains(signedHeader)) {
                // Convert and add header values as per
                // https://docs.aws.amazon.com/general/latest/gr/sigv4-create-canonical-request.html
                // * Header having multiple values should be converted to comma separated values.
                // * Multi-spaced value of header should be trimmed to single spaced value.
                canonicalHeaders.put(
                        signedHeader,
                        headers.get(name).stream()
                                .map(
                                        value -> {
                                            return value.replaceAll("( +)", " ");
                                        })
                                .collect(Collectors.joining(",")));
            }
        }
        return canonicalHeaders;
    }

    /**
     * 计算signedHeaders
     * @return
     */
    public static String buildSignedHeaders(Map<String, String> canonicalHeaders) {
        return Joiner.on(";").join(canonicalHeaders.keySet());
    }

    public static String buildCanonicalQueryString(String params){

        if(StrUtil.isBlank(params)){
            return "";
        }

        // Building a multimap which only order keys, ordering values is not performed
        // until MinIO server supports it.
        Multimap<String, String> signedQueryParams =
                MultimapBuilder.treeKeys().arrayListValues().build();

        for (String queryParam : params.split("&")) {
            String[] tokens = queryParam.split("=");
            if (tokens.length > 1) {
                signedQueryParams.put(tokens[0], tokens[1]);
            } else {
                signedQueryParams.put(tokens[0], "");
            }
        }

        return Joiner.on("&").withKeyValueSeparator("=").join(signedQueryParams.entries());

    }

    public static String buildCanonicalRequest(Map<String, String> canonicalHeaders,String signedHeaders
            ,String canonicalQueryString,String method,String path) throws NoSuchAlgorithmException {

        String canonicalRequest = method + "\n" + path + "\n" + canonicalQueryString + "\n"
                + Joiner.on("\n").withKeyValueSeparator(":").join(canonicalHeaders) + "\n\n" + signedHeaders + "\n" + ZERO_SHA256_HASH;
        System.out.println("canonicalRequest="+canonicalRequest);
        return Digest.sha256Hash(canonicalRequest);
    }

    /**
     * 计算stringToSign
     * @param date
     * @param scope
     * @param canonicalRequestHash
     * @return
     */
    public static String buildStringToSign(ZonedDateTime date,String scope,String canonicalRequestHash){
        return  "AWS4-HMAC-SHA256" + "\n" + date.format(Time.AMZ_DATE_FORMAT) + "\n" + scope + "\n" + canonicalRequestHash;
    }

    /**
     * 计算signingKey
     * @param serviceName
     * @param date
     * @return
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeyException
     */
    public static byte[] buildSigningKey(String serviceName,ZonedDateTime date) throws NoSuchAlgorithmException, InvalidKeyException {
        String aws4SecretKey = "AWS4" + secretKey;
        byte[] dateKey = sumHmac(aws4SecretKey.getBytes(StandardCharsets.UTF_8), date.format(Time.SIGNER_DATE_FORMAT).getBytes(StandardCharsets.UTF_8));
        byte[] dateRegionKey = sumHmac(dateKey, US_EAST_1.getBytes(StandardCharsets.UTF_8));
        byte[] dateRegionServiceKey = sumHmac(dateRegionKey, serviceName.getBytes(StandardCharsets.UTF_8));
        return sumHmac(dateRegionServiceKey, "aws4_request".getBytes(StandardCharsets.UTF_8));
    }

    /**
     * 计算signature
     * @param signingKey
     * @param stringToSign
     * @return
     */
    public static String buildSignature(byte[] signingKey,String stringToSign) throws NoSuchAlgorithmException, InvalidKeyException {
        byte[] digest = sumHmac(signingKey, stringToSign.getBytes(StandardCharsets.UTF_8));
        return BaseEncoding.base16().encode(digest).toLowerCase(Locale.US);
    }

    /**
     * 计算authorization
     * @param accessKey
     * @param scope
     * @param signedHeaders
     * @param signature
     * @return
     */
    public static String buildAuthorization(String accessKey,String scope,String signedHeaders,String signature){
        return "AWS4-HMAC-SHA256 Credential=" + accessKey + "/" + scope + ", SignedHeaders=" + signedHeaders + ", Signature=" + signature;
    }

    private static byte[] sumHmac(byte[] key, byte[] data) throws NoSuchAlgorithmException, InvalidKeyException {
        Mac mac = Mac.getInstance("HmacSHA256");

        mac.init(new SecretKeySpec(key, "HmacSHA256"));
        mac.update(data);

        return mac.doFinal();
    }

}