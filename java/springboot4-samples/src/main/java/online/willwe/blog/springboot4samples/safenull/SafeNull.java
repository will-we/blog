package online.willwe.blog.springboot4samples.safenull;

import org.jspecify.annotations.Nullable;

@SuppressWarnings("unused")
public class SafeNull {
    public String getUserName(@Nullable String user) {
        // 此处存在NPE，使用NullAway在编译期检查
        // if (user == null) {
        //     return "0";
        // }
        return Integer.toString(user.length());
    }
}