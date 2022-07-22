package com.info.serde;

import com.info.types.UserDetails;
import com.info.types.UserLogin;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;

import java.util.HashMap;
import java.util.Map;

public class AppSerdes extends Serdes {

    static final class UserDetailsSerde extends WrapperSerde<UserDetails> {
        UserDetailsSerde() {
            super(new JsonSerializer<>(), new JsonDeserializer<>());
        }
    }

    public static Serde<UserDetails> UserDetails() {
        UserDetailsSerde serde = new UserDetailsSerde();

        Map<String, Object> serdeConfigs = new HashMap<>();
        serdeConfigs.put(JsonDeserializer.VALUE_CLASS_NAME_CONFIG, UserDetails.class);
        serde.configure(serdeConfigs, false);

        return serde;
    }

    static final class UserLoginSerde extends WrapperSerde<UserLogin> {
        UserLoginSerde() {
            super(new JsonSerializer<>(), new JsonDeserializer<>());
        }
    }

    public static Serde<UserLogin> UserLogin() {
        UserLoginSerde serde = new UserLoginSerde();

        Map<String, Object> serdeConfigs = new HashMap<>();
        serdeConfigs.put(JsonDeserializer.VALUE_CLASS_NAME_CONFIG, UserLogin.class);
        serde.configure(serdeConfigs, false);

        return serde;
    }

}
