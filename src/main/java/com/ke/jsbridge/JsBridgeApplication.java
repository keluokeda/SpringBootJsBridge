package com.ke.jsbridge;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.graalvm.polyglot.Context;
import org.jetbrains.annotations.NotNull;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.util.FileCopyUtils;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;

@RestController
@SpringBootApplication
public class JsBridgeApplication {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @GetMapping(value = "/greet", produces = "application/json;charset=UTF-8")
    public Object greet() throws IOException {
        var context = Context.newBuilder().allowAllAccess(true).build();

        var js = readJsFile();
        context.eval("js", js);
//        var result = context.eval("js", "greet('Java')").asString();

        var map = new HashMap<String, Object>();
        map.put("realIP", "39.144.7.80");
        map.put("id", "1880562045");

        return context.eval("js", "NeteaseCloudMusicApi.beforeRequest('song_url_v1','" + objectMapper.writeValueAsString(map) + "')").asString();
//        return result;
//        return context.eval("js", "NeteaseCloudMusicApi.beforeRequest('song_url_v1','{\"id\": '1880562045', \"level\": \"exhigh\", \"cookie\": \"_ga=GA1.1.300900204.1652408497; Studio-61845bdd=8a58e771-05c5-48b9-9c11-324b5e670dbb; Webstorm-e115b33c=69d97e48-144c-430f-be08-2cedb3b06409; Hm_lvt_0b1b1ffb8b22eff4e72970474d2f42b7=1700014428,1700462845,1702002664; NMTID=00Oy0B1_LyhpfoOeUDpsOY9jnSFiY8AAAGMjSZvFw; MUSIC_U=00FFF073C038CE271DB037ECE82F77E00ED2CB9A4E0C81494D2D7AD5FE98595F4443CB77C64AFA4D63794BBA67C24F6C79C2BD75239885679D338E194EEC176A6D08CD946D7C8F2DFC2C2AF6C725BAC9C8CBD97957A0CD56D6BCE2A9C060768EC288A358158EF73B54FD1D88DBDCAA01AAC42EEA88E2F58EA737A95F89BF00D2DE7926E1A86E94756E7C718D0C532B57CCDDD8F474D54BE40D8D0FE3642A7D4929FC5CA2B2D0259EC0A4A4487EA591716E876C1CFECF526435AF5FC2D231C7437DFE60514F68F98C57D32D65A9C6002F86679EEE83E2D840CB5C7DDEC59D6FF193EF2231A9BEBF89F03878AB4AEB2F7B6CE492EEC0F5FE21526DC2492C035EF88D1EB8B252A243BE48DF4FE1720B2E8352ACEB3615A77320F13F7B5B1E95C12EF12E69232A13814A228F2D76690745F7CA6747BDB72245C8279A1C3284F8430475EBB0FB9CD51D20FC861A0F217CEE2CCA; __csrf=a343b629348239fe0ea53faa499db5cd\", \"realIP\": \"116.25.146.177\"}')").toString();
    }


    @NotNull
    private String readJsFile() throws IOException {
        var file = ResourceUtils.getFile("classpath:static/NeteaseCloudMusicApi.js");
        var inputStream = Files.newInputStream(file.toPath());
        var bytes = FileCopyUtils.copyToByteArray(inputStream);

        return new String(bytes, StandardCharsets.UTF_8);
    }

    public static void main(String[] args) {
        SpringApplication.run(JsBridgeApplication.class, args);
    }

}
