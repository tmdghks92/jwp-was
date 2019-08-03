package webserver.http;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.DataOutputStream;
import java.io.IOException;

public class HttpResponse {
    private static final Logger logger = LoggerFactory.getLogger(HttpResponse.class);
    private byte[] body;
    private Cookie cookie;

    public HttpResponse() {
        this.cookie = new Cookie();
    }

    public HttpResponse(byte[] body) {
        this.body = body;
        this.cookie = new Cookie();
    }

    public byte[] getBody() {
        return this.body;
    }

    public void response200Header(DataOutputStream dos) {
        try {
            dos.writeBytes("HTTP/1.1 200 OK \r\n");
            dos.writeBytes("Content-Type: text/html;charset=utf-8\r\n");
            dos.writeBytes("Content-Length: " + body.length + "\r\n");
            setCookies(dos);
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    public void response302Header(DataOutputStream dos, String redirectPath) {
        try {
            dos.writeBytes("HTTP/1.1 302 Found \r\n");
            dos.writeBytes("Location: " + redirectPath + "\r\n");
            setCookies(dos);
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    public void responseBody(DataOutputStream dos, byte[] body) {
        try {
            dos.write(body, 0, body.length);
            dos.flush();
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    public void setBody(byte[] body) {
        this.body = body;
    }

    public Cookie getCookie() {
        return cookie;
    }

    private void setCookies(DataOutputStream dos) {
        if (cookie.isEmpty()) return;

        String setCookieFormat = "Set-Cookie: %s=%s; Path=/\r\n";
        cookie.keySet().forEach(key -> {
            try {
                dos.writeBytes(String.format(setCookieFormat, key, cookie.get(key)));
            } catch (IOException e) {
                logger.error(e.getMessage());
            }
        });
    }
}
