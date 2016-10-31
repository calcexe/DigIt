package pl.calc_exe.wykop.model.rest.extras;

import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import pl.calc_exe.wykop.extras.Extras;
import pl.calc_exe.wykop.extras.HashUtils;

/**
 * Class which generating APISIGN (key based on post parameters required by api).
 * APISIGN structure: md5(SECRET + URL + POST_PARAMETERS).
 * It's not allow to read post parameters inside interceptor, so they are given in headers.
 * This class change all given headers (POST parameters) to apisign header.
 */
public class ApisignInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request original = chain.request();
        Headers headers = original.headers();

        StringBuilder sb = new StringBuilder();
        sb.append(Extras.SECRET);
        sb.append(original.url().toString());

        FormBody.Builder body = new FormBody.Builder();
        Request.Builder request = original.newBuilder();

        for (int i = 0; i < headers.size(); i++) {
            body.add(headers.name(i), headers.value(i));
            sb.append(headers.value(i));
            request.removeHeader(headers.name(i));
        }

        request.post(body.build());

        String apisign = HashUtils.toMd5(sb.toString());

        request.addHeader(Extras.API_SIGN, apisign == null ? "" : apisign);

        return chain.proceed(request.build());
    }
}
