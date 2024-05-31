//import cn.hutool.core.collection.CollUtil;
//import cn.hutool.core.util.StrUtil;
//import cn.hutool.http.HttpRequest;
//import cn.hutool.http.HttpResponse;
//import cn.hutool.http.HttpUtil;
//import cn.hutool.http.Method;
//import com.google.common.base.Joiner;
//import com.google.common.collect.ImmutableSet;
//import com.google.common.collect.Multimap;
//import com.google.common.collect.MultimapBuilder;
//import com.google.common.io.BaseEncoding;
//import io.minio.PartSource;
//import io.minio.Signer;
//import io.minio.Xml;
//import io.minio.credentials.Credentials;
//import io.minio.errors.*;
//import io.minio.http.HttpUtils;
//import io.minio.messages.ErrorResponse;
//import okhttp3.*;
//
//import javax.crypto.Mac;
//import javax.crypto.spec.SecretKeySpec;
//import java.io.IOException;
//import java.io.PrintWriter;
//import java.nio.charset.StandardCharsets;
//import java.security.InvalidKeyException;
//import java.security.NoSuchAlgorithmException;
//import java.time.ZonedDateTime;
//import java.util.*;
//import java.util.stream.Collectors;
//
//public class MinIORestful2 {
//
//    static final String serviceName = "s3";
//    static final String US_EAST_1 = "us-east-1";
//    private static final Set<String> IGNORED_HEADERS = ImmutableSet.of("accept-encoding", "authorization", "content-type", "content-length", "user-agent");
//
//    static String accessKey = "minioadmin";
//    static String secretKey = "minioadmin";
//    static String ZERO_MD5_HASH = "1B2M2Y8AsgTpgAmY7PhCfg==";
//    static String ZERO_SHA256_HASH = "e3b0c44298fc1c149afbf4c8996fb92427ae41e4649b934ca495991b7852b855";
//    static String backend = "http://localhost:9000";
//
//    public static void main(String[] args) throws NoSuchAlgorithmException, InvalidKeyException {
//
//        // 动态传入参数
//        String url = "http://localhost:9000/document";
//
//        HttpResponse httpResponse = request(url,"","/document","head");
//
//        System.out.println("httpResponse.isOk()="+httpResponse.isOk());
//        System.out.println("httpResponse.getStatus()="+httpResponse.getStatus());
//        System.out.println("httpResponse.headers()="+httpResponse.headers());
//        System.out.println("httpResponse.body()="+httpResponse.body());
//
//    }
//
//    public static HttpResponse request(String url,String params,String path,String method) throws NoSuchAlgorithmException, InvalidKeyException {
//
//        // 取得当前时间
//        ZonedDateTime date = ZonedDateTime.now();
//
//        // 创建除Authorization外所有header
//        Map<String, List<String>> headers = new HashMap<>();
//        headers.put("Host", CollUtil.newArrayList(backend.replace("http://","").replace("https://","")));
//        headers.put("Accept-Encoding", CollUtil.newArrayList("identity"));
//        headers.put("User-Agent", CollUtil.newArrayList("MinIO (Windows 10; amd64) minio-java/8.3.3"));
//        headers.put("Content-MD5", CollUtil.newArrayList(ZERO_MD5_HASH));
//        headers.put("x-amz-content-sha256", CollUtil.newArrayList(ZERO_SHA256_HASH));
//        headers.put("x-amz-date", CollUtil.newArrayList(date.format(Time.AMZ_DATE_FORMAT)));
//
//        // 计算scope
//        String scope =buildScope(serviceName,date);
//
//        // 计算signedHeaders
//        Map<String, String> canonicalHeaders = buildCanonicalHeaders(headers,IGNORED_HEADERS);
//
//        // 计算signedHeaders
//        String signedHeaders = buildSignedHeaders(canonicalHeaders);
//
//        // 计算canonicalQueryString
//        String canonicalQueryString = buildCanonicalQueryString(params);
//
//        // 计算buildCanonicalRequest
//        String canonicalRequestHash = buildCanonicalRequest(canonicalHeaders,signedHeaders,canonicalQueryString,method,path);
//
//        // 计算stringToSign
//        String stringToSign = buildStringToSign(date,scope,canonicalRequestHash);
//
//        // 计算signingKey
//        byte[] signingKey = buildSigningKey(serviceName,date);
//
//        // 计算signature
//        String signature = buildSignature(signingKey,stringToSign);
//
//        // 计算authorization
//        String authorization = buildAuthorization(accessKey,scope,signedHeaders,signature);
//
//        HttpRequest httpRequest = HttpUtil.createRequest(Method.HEAD, url);
//        httpRequest.header(headers,true);
//        httpRequest.header("Authorization", authorization);
//
//
//
//        System.out.println("scope="+scope);
//        System.out.println("canonicalHeaders="+canonicalHeaders);
//        System.out.println("signedHeaders="+signedHeaders);
//        System.out.println("canonicalQueryString="+canonicalQueryString);
//        System.out.println("canonicalRequestHash="+canonicalRequestHash);
//        System.out.println("stringToSign="+stringToSign);
//        System.out.println("signingKey="+signingKey);
//        System.out.println("signature="+signature);
//        System.out.println("authorization="+authorization);
//
//        System.out.println("httpRequest.headers()="+httpRequest.headers());
//
//        return httpRequest.execute();
//    }
//
//
//    protected void execute(
//            io.minio.http.Method method,
//            String bucketName,
//            String objectName,
//            String region,
//            Headers headers,
//            Multimap<String, String> queryParamMap,
//            Object body,
//            int length)
//            throws ErrorResponseException, InsufficientDataException, InternalException,
//            InvalidKeyException, InvalidResponseException, IOException, NoSuchAlgorithmException,
//            ServerException, XmlParserException {
//        boolean traceRequestBody = false;
//        if (body != null && !(body instanceof PartSource || body instanceof byte[])) {
//            byte[] bytes;
//            if (body instanceof CharSequence) {
//                bytes = body.toString().getBytes(StandardCharsets.UTF_8);
//            } else {
//                bytes = Xml.marshal(body).getBytes(StandardCharsets.UTF_8);
//            }
//
//            body = bytes;
//            length = bytes.length;
//            traceRequestBody = true;
//        }
//
//        if (body == null && (method == io.minio.http.Method.PUT || method == io.minio.http.Method.POST))
//            body = HttpUtils.EMPTY_BODY;
//
//        HttpUrl url = buildUrl(method, bucketName, objectName, region, queryParamMap);
//        Credentials creds = (provider == null) ? null : provider.fetch();
//        Request request = createRequest(url, method, headers, body, length, creds);
//        if (creds != null) {
//            request =
//                    Signer.signV4S3(
//                            request,
//                            region,
//                            creds.accessKey(),
//                            creds.secretKey(),
//                            request.header("x-amz-content-sha256"));
//        }
//
//        StringBuilder traceBuilder =
//                newTraceBuilder(
//                        request, traceRequestBody ? new String((byte[]) body, StandardCharsets.UTF_8) : null);
//        PrintWriter traceStream = this.traceStream;
//        if (traceStream != null) traceStream.println(traceBuilder.toString());
//        traceBuilder.append("\n");
//
//        OkHttpClient httpClient = this.httpClient;
//        if (!(body instanceof byte[]) && (method == io.minio.http.Method.PUT || method == io.minio.http.Method.POST)) {
//            // Issue #924: disable connection retry for PUT and POST methods for other than byte array.
//            httpClient = this.httpClient.newBuilder().retryOnConnectionFailure(false).build();
//        }
//
//        Response response = httpClient.newCall(request).execute();
////        String trace =
////                response.protocol().toString().toUpperCase(Locale.US)
////                        + " "
////                        + response.code()
////                        + "\n"
////                        + response.headers();
////        traceBuilder.append(trace).append("\n");
////        if (traceStream != null) traceStream.println(trace);
////
////        if (response.isSuccessful()) {
////            if (traceStream != null) {
////                // Trace response body only if the request is not GetObject/ListenBucketNotification S3 API.
////                Set<String> keys = queryParamMap.keySet();
////                if ((method != io.minio.http.Method.GET
////                        || objectName == null
////                        || !Collections.disjoint(keys, TRACE_QUERY_PARAMS))
////                        && !(keys.contains("events") && (keys.contains("prefix") || keys.contains("suffix")))) {
////                    ResponseBody responseBody = response.peekBody(1024 * 1024);
////                    traceStream.println(responseBody.string());
////                }
////                traceStream.println(END_HTTP);
////            }
////            return response;
////        }
////
////        String errorXml = null;
////        try (ResponseBody responseBody = response.body()) {
////            errorXml = responseBody.string();
////        }
////
////        if (!("".equals(errorXml) && method.equals(io.minio.http.Method.HEAD))) {
////            traceBuilder.append(errorXml).append("\n");
////            if (traceStream != null) traceStream.println(errorXml);
////        }
////
////        traceBuilder.append(END_HTTP).append("\n");
////        if (traceStream != null) traceStream.println(END_HTTP);
////
////        // Error in case of Non-XML response from server for non-HEAD requests.
////        String contentType = response.headers().get("content-type");
////        if (!method.equals(io.minio.http.Method.HEAD)
////                && (contentType == null
////                || !Arrays.asList(contentType.split(";")).contains("application/xml"))) {
////            throw new InvalidResponseException(
////                    response.code(),
////                    contentType,
////                    errorXml.substring(0, errorXml.length() > 1024 ? 1024 : errorXml.length()),
////                    traceBuilder.toString());
////        }
////
////        ErrorResponse errorResponse = null;
////        if (!"".equals(errorXml)) {
////            errorResponse = Xml.unmarshal(ErrorResponse.class, errorXml);
////        } else if (!method.equals(io.minio.http.Method.HEAD)) {
////            throw new InvalidResponseException(
////                    response.code(), contentType, errorXml, traceBuilder.toString());
////        }
////
////        if (errorResponse == null) {
////            String code = null;
////            String message = null;
////            switch (response.code()) {
////                case 301:
////                case 307:
////                case 400:
////                    String[] result = handleRedirectResponse(method, bucketName, response, true);
////                    code = result[0];
////                    message = result[1];
////                    break;
////                case 404:
////                    if (objectName != null) {
////                        code = "NoSuchKey";
////                        message = "Object does not exist";
////                    } else if (bucketName != null) {
////                        code = NO_SUCH_BUCKET;
////                        message = NO_SUCH_BUCKET_MESSAGE;
////                    } else {
////                        code = "ResourceNotFound";
////                        message = "Request resource not found";
////                    }
////                    break;
////                case 501:
////                case 405:
////                    code = "MethodNotAllowed";
////                    message = "The specified method is not allowed against this resource";
////                    break;
////                case 409:
////                    if (bucketName != null) {
////                        code = NO_SUCH_BUCKET;
////                        message = NO_SUCH_BUCKET_MESSAGE;
////                    } else {
////                        code = "ResourceConflict";
////                        message = "Request resource conflicts";
////                    }
////                    break;
////                case 403:
////                    code = "AccessDenied";
////                    message = "Access denied";
////                    break;
////                case 412:
////                    code = "PreconditionFailed";
////                    message = "At least one of the preconditions you specified did not hold";
////                    break;
////                case 416:
////                    code = "InvalidRange";
////                    message = "The requested range cannot be satisfied";
////                    break;
////                default:
////                    if (response.code() >= 500) {
////                        throw new ServerException(
////                                "server failed with HTTP status code " + response.code(), traceBuilder.toString());
////                    }
////
////                    throw new InternalException(
////                            "unhandled HTTP code "
////                                    + response.code()
////                                    + ".  Please report this issue at "
////                                    + "https://github.com/minio/minio-java/issues",
////                            traceBuilder.toString());
////            }
////
////            errorResponse =
////                    new ErrorResponse(
////                            code,
////                            message,
////                            bucketName,
////                            objectName,
////                            request.url().encodedPath(),
////                            response.header("x-amz-request-id"),
////                            response.header("x-amz-id-2"));
////        }
////
////        // invalidate region cache if needed
////        if (errorResponse.code().equals(NO_SUCH_BUCKET) || errorResponse.code().equals(RETRY_HEAD)) {
////            regionCache.remove(bucketName);
////        }
////
////        throw new ErrorResponseException(errorResponse, response, traceBuilder.toString());
//    }
//
//}