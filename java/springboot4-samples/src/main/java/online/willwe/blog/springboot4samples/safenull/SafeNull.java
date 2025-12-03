package online.willwe.blog.springboot4samples.safenull;

import org.jspecify.annotations.Nullable;

@SuppressWarnings("unused")
public class SafeNull {
    public String getUserName(@Nullable String user) {
        if (user == null) {
            return "0";
        }
        return Integer.toString(user.length());
    }
}