package br.com.avila.serializer.convert;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.AbstractJackson2HttpMessageConverter;

public final class YamlJackson2HttpMessageConvert extends AbstractJackson2HttpMessageConverter {

      protected YamlJackson2HttpMessageConvert() {
        super(new YAMLMapper()
                .setSerializationInclusion(JsonInclude.Include.NON_NULL), MediaType.parseMediaType("application/yaml"));
    }
}
