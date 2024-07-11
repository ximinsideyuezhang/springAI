package com.kdx.springai.controller;


import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.image.ImagePrompt;
import org.springframework.ai.image.ImageResponse;
import org.springframework.ai.openai.*;
import org.springframework.ai.openai.api.OpenAiApi;
import org.springframework.ai.openai.api.OpenAiAudioApi;
import org.springframework.ai.openai.audio.speech.SpeechPrompt;
import org.springframework.ai.openai.audio.speech.SpeechResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.io.FileOutputStream;
import java.io.IOException;

@RestController
@RequestMapping("/ai")
public class HelloController {


    @Autowired
    private ChatClient chatClient;

    @Autowired(required = false)
    private ChatModel chatModel;

    @Autowired(required = false)
    private OpenAiImageModel openaiImageModel;

    @Autowired(required = false)
    private OpenAiAudioSpeechModel openAiAudioSpeechModel;

    @RequestMapping("/hello")
    public String hello() {
        return "hello";
    }

    //交流
    @RequestMapping("/chat")
    public String generation(@RequestParam(value = "message", defaultValue = "讲个笑话") String message) {

        //prompt：提示词
        return this.chatClient.prompt()
                //用户信息
                .user(message)
                //请求大模型
                .call()
                //返回文本
                .content();
    }

    //流式响应
    @RequestMapping(value = "/stream", produces = "text/html;charset=UTF-8")
    public Flux<String> stream(@RequestParam(value = "message", defaultValue = "讲个笑话") String message) {
        Flux<String> output = chatClient.prompt()
                .user(message)
                //流式调用
                .stream()
                .content();
        return output;
    }

    //chatModel api
    @RequestMapping(value = "/ChatResponse", produces = "text/html;charset=UTF-8")
    public String ChatResponse(@RequestParam(value = "message") String message) {
        ChatResponse response = chatModel.call(new Prompt(
                message,
                OpenAiChatOptions.builder()
                        //选择gpt版本
                        .withModel("gpt-4-32k")
                        .withTemperature(0.4f)
                        .build()
        ));
        return response.getResult().getOutput().getContent();
    }

    //文生图
    @RequestMapping(value = "/openaiImageModel", produces = "text/html;charset=UTF-8")
    public String openaiImageModel(@RequestParam(value = "message") String message) {
        ImageResponse response = openaiImageModel.call(
                new ImagePrompt(message,
                        OpenAiImageOptions.builder()
                                //图片质量
                                .withQuality("hd")
                                //生成几张
                                .withN(1)
                                //尺寸
                                .withHeight(1024)
                                .withWidth(1024).build())

        );
        return response.getResult().getOutput().getUrl();
    }


    //文生语音
    @RequestMapping(value = "/writeByte", produces = "text/html;charset=UTF-8")
    public String writeByte(@RequestParam(value = "message") String message) {
        OpenAiAudioSpeechOptions speechOptions = OpenAiAudioSpeechOptions.builder()
                .withModel(OpenAiAudioApi.TtsModel.TTS_1.value)
                .withVoice(OpenAiAudioApi.SpeechRequest.Voice.ALLOY)
                .withResponseFormat(OpenAiAudioApi.SpeechRequest.AudioResponseFormat.MP3)
                .withSpeed(1.0f)
                .build();

        SpeechPrompt speechPrompt = new SpeechPrompt(message, speechOptions);
        SpeechResponse response = openAiAudioSpeechModel.call(speechPrompt);

        byte[] body = response.getResult().getOutput();
        try {
            writeByte(body,"D:\\Project");
        } catch (Exception e) {
            System.out.println(e);
        }
        return "ok";
    }

    public static void writeByte(byte[] audioBytes, String outputFilePath) throws IOException {
        FileOutputStream fileOutputStream = new FileOutputStream(outputFilePath + "111.mp3");
        fileOutputStream.write(audioBytes);
        fileOutputStream.close();
    }


    //functionCall
    @RequestMapping(value = "/functionCall", produces = "text/html;charset=UTF-8")
    public String functionCall(@RequestParam(value = "message") String message) {
        OpenAiChatOptions aiChatOptions = OpenAiChatOptions.builder()
                //设置实现了function接口的bean名称
                .withFunction("LocationNameFunction")
                .withModel(OpenAiApi.ChatModel.GPT_3_5_TURBO)
                .build();
        ChatResponse response = chatModel.call(new Prompt(message, aiChatOptions));
        return response.getResult().getOutput().getContent();
    }

}
