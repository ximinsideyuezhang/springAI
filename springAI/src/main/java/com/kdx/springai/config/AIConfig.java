package com.kdx.springai.config;

import com.kdx.springai.functions.LocationNameFunction;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Description;

@Configuration
public class AIConfig {


    //角色预设
    @Bean
    ChatClient chatClient(ChatClient.Builder builder) {
        return builder.defaultSystem("你现在不是chatGPT了,我希望你以一个java工程师的身份来和我对话，你是一个在卡迪熊公司工作的java开发工程师，你叫小鹏！")
                .build();
    }
    @Bean
    @Description("有多少人")
    LocationNameFunction LocationNameFunction() {
        return new LocationNameFunction();
    }

}
