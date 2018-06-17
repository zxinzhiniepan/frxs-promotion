/*
 * frxs Inc.  兴盛社区网络服务股份有限公司.
 * Copyright (c) 2017-2018. All Rights Reserved.
 */

package com.frxs.promotion.common.util;

import java.io.IOException;
import java.net.InetAddress;
import java.net.URISyntaxException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.io.IOUtils;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.FormBodyPart;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.client.LaxRedirectStrategy;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.Args;
import org.apache.http.util.EntityUtils;

/**
 * Http工具类
 *
 * Created by Colby.liu on 15/4/18.
 */
public class HttpCall {

  public static final String UTF_8 = "UTF-8";

  private final static String USER_AGENT = "Mozilla/5.0 (iPhone; CPU iPhone OS 6_0 like Mac OS X) AppleWebKit/536.26 (KHTML, like Gecko) Safari";

  private final static PoolingHttpClientConnectionManager CONNECTION_MANAGER = new PoolingHttpClientConnectionManager();

  static {
    CONNECTION_MANAGER.setDefaultMaxPerRoute(512);
    CONNECTION_MANAGER.setMaxTotal(1024);
    CONNECTION_MANAGER.setValidateAfterInactivity(10 * 1000);
  }

  public final static HttpCall INSTANCE = new HttpCall();

  private final CloseableHttpClient httpClient;
  private final HttpContext context;

  public HttpCall() {
    this(null);
  }

  /**
   * Custom init.
   *
   * @param params custom params, supports: - "localAddress" = "1.2.3.4" - "proxy" = "1.2.3.4:8080"
   */
  public HttpCall(Map<String, Object> params) {
    int connectTimeout = 60000;
    int socketTimeout = 60000;
    RequestConfig.Builder reqConfigBuilder = RequestConfig.custom();
    if (params != null) {
      connectTimeout = params.containsKey("connectTimeout") ? (int) params.get("connectTimeout") : connectTimeout;
      socketTimeout = params.containsKey("socketTimeout") ? (int) params.get("socketTimeout") : socketTimeout;
      try {
        Object val;
        if ((val = params.get("localAddress")) != null) {
          reqConfigBuilder.setLocalAddress(InetAddress.getByName(String.valueOf(val)));
        }
        if ((val = params.get("proxy")) != null) {
          String[] sa = String.valueOf(val).split(":");
          reqConfigBuilder.setProxy(new HttpHost(sa[0], Integer.parseInt(sa[1])));
        }
      } catch (UnknownHostException e) {
        throw new RuntimeException(e);
      }
    }

    reqConfigBuilder.setConnectTimeout(connectTimeout)
        .setSocketTimeout(socketTimeout)
        .setMaxRedirects(5);
    httpClient = HttpClients.custom()
        .setConnectionManager(CONNECTION_MANAGER)
        .setRedirectStrategy(new LaxRedirectStrategy())
        .setDefaultRequestConfig(reqConfigBuilder.build())
        .setUserAgent(USER_AGENT)
        .build();
    context = HttpClientContext.create();
  }

  /**
   * Fetch resource with Http Get method
   *
   * @param _url NotNull
   * @param headers Http Headers, can be null
   * @param params Http parameters, can be null
   */
  public String get(String _url, Map<String, Object> headers, Map<String, Object> params) throws IOException {
    Args.notNull(_url, "url is required");
    HttpGet httpGet = null;
    try {
      URIBuilder uri = new URIBuilder(_url);
      if (params != null) {
        for (Map.Entry<String, Object> param : params.entrySet()) {
          String name = param.getKey();
          Object value = param.getValue();
          if (value instanceof List) {
            for (Object item : ((List) value)) {
              uri.addParameter(name, String.valueOf(item));
            }
          } else {
            uri.addParameter(name, String.valueOf(value));
          }
        }
      }
      httpGet = new HttpGet(uri.build());
      if (headers != null) {
        for (Map.Entry<String, Object> header : headers.entrySet()) {
          httpGet.addHeader(header.getKey(), String.valueOf(header.getValue()));
        }
      }
    } catch (URISyntaxException e) {
      throw new RuntimeException(e);
    }

    CloseableHttpResponse response = null;
    try {
      response = httpClient.execute(httpGet, context);
      checkStatus(_url, response);
      HttpEntity entity = response.getEntity();
      return EntityUtils.toString(entity);
    } finally {
      IOUtils.closeQuietly(response);
    }
  }

  /**
   * Http Post --- LOW LEVEL METHOD ---
   *
   * @param _url NotNull
   * @param headers Http Headers, can be null
   * @param reqEntity Http request entity, can be null
   */
  public CloseableHttpResponse post(String _url, Map<String, Object> headers, HttpEntity reqEntity) throws IOException {
    Args.notNull(_url, "url is required");
    HttpPost httpPost = new HttpPost(_url);
    if (reqEntity != null) {
      httpPost.setEntity(reqEntity);
    }
    if (headers != null) {
      for (Map.Entry<String, Object> header : headers.entrySet()) {
        httpPost.addHeader(header.getKey(), String.valueOf(header.getValue()));
      }
    }
    CloseableHttpResponse response = httpClient.execute(httpPost, context);
    checkStatus(_url, response);
    return response;
  }


  /**
   * Simple Http Post method
   *
   * @param _url NotNull
   * @param headers Http Headers, can be null
   * @param _params Http request parameters, can be null
   */
  public String post(String _url, Map<String, Object> headers, Map<String, Object> _params) throws IOException {
    HttpEntity postBody = null;
    if (_params != null) {
      postBody = encodeParameters(_params);
    }
    CloseableHttpResponse resp = null;
    try {
      resp = post(_url, headers, postBody);
      return EntityUtils.toString(resp.getEntity(), UTF_8);
    } finally {
      IOUtils.closeQuietly(resp);
    }
  }


  /**
   * Http Post method with Multipart/form-data
   *
   * @param _url NotNull
   * @param headers Http Headers, can be null
   * @param _params Http request parameters, can be null. You might need MultiValueMap to submit key with multi-values. <li><code>key=value</code> for text values;</li>
   * <li><code>key=org.apache.http.entity.mime.FormBodyPart</code> for File/Binary values;</li> <p> Examples: <li><code>params.put("id", "abc")  // for text</code></li> <li><code> FormBodyPart part =
   * FormBodyPartBuilder.create("xxx", new FileBody(new File("xxx"))).build(); params.put("file", part) // for file/binary </code></li>
   */
  public String postMultipart(String _url, Map<String, Object> headers, Map<String, ?> _params) throws IOException {
    HttpEntity postBody = null;
    if (_params != null) {
      MultipartEntityBuilder builder = MultipartEntityBuilder.create();
      processMultipart(builder, null, _params);
      postBody = builder.build();
      if (headers == null) {
        headers = new HashMap<>();
      }
      Header contentType = postBody.getContentType();
      headers.put(contentType.getName(), contentType.getValue());
    }
    CloseableHttpResponse resp = null;
    try {
      resp = post(_url, headers, postBody);
      return EntityUtils.toString(resp.getEntity(), UTF_8);
    } finally {
      IOUtils.closeQuietly(resp);
    }
  }

  private static void checkStatus(String _url, HttpResponse response) {
    StatusLine status = response.getStatusLine();
    if (status.getStatusCode() / 100 != 2) {
      HttpEntity entity = response.getEntity();
      String body = null;
      try {
        body = EntityUtils.toString(entity);
      } catch (IOException ignored) {
      }
      throw new IllegalStateException(String.format("Request [%s] failed. StatusLine: %s. Response: %s", _url, status, body));
    }
  }

  private static StringEntity encodeParameters(Map<String, Object> params) {
    List<NameValuePair> nameValuePairs = new ArrayList<>();
    for (Map.Entry<String, Object> entry : params.entrySet()) {
      String name = entry.getKey();
      Object value = entry.getValue();
      if (value instanceof List) {
        for (Object item : ((List) value)) {
          nameValuePairs.add(new BasicNameValuePair(name, String.valueOf(item)));
        }
      } else {
        nameValuePairs.add(new BasicNameValuePair(name, String.valueOf(value)));
      }
    }
    return new StringEntity(URLEncodedUtils.format(nameValuePairs, UTF_8),
        ContentType.create(URLEncodedUtils.CONTENT_TYPE, UTF_8));
  }

  private static void processMultipart(MultipartEntityBuilder builder, String name, Object param) {
    if (param instanceof FormBodyPart) {
      builder.addPart(((FormBodyPart) param));
    } else if (param instanceof List) {
      for (Object item : ((List) param)) {
        processMultipart(builder, name, item);
      }
    } else if (param instanceof Map) {
      for (Map.Entry<String, ?> entry : ((Map<String, ?>) param).entrySet()) {
        processMultipart(builder, entry.getKey(), entry.getValue());
      }
    } else {
      builder.addTextBody(name, String.valueOf(param));
    }
  }
}
