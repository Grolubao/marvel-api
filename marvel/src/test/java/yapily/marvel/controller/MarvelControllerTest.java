package yapily.marvel.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import yapily.marvel.exceptions.MarvelApiKeyNotFoundException;
import yapily.marvel.exceptions.MissingJsonNodeException;
import yapily.marvel.exceptions.UnableToProcessJsonException;
import yapily.marvel.exceptions.YandexApiKeyNotFoundException;
import yapily.marvel.model.MarvelCharacter;
import yapily.marvel.service.MarvelService;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
class MarvelControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MarvelService marvelService;

    @Test
    public void getCharacters_should_return_ok() throws Exception {
        List<Long> characterIds = new ArrayList<>();
        characterIds.add(100L);
        characterIds.add(200L);

        when(marvelService.getCharactersIds()).thenReturn(characterIds);
        this.mockMvc.perform(get("/characters")).andExpect(status().isOk())
                .andExpect(content().string("[100,200]"));
    }

    @Test
    public void getCharacters_will_return_exception_if_no_api_key() throws Exception {
        List<Long> characterIds = new ArrayList<>();
        characterIds.add(100L);
        characterIds.add(200L);

        when(marvelService.getCharactersIds()).thenThrow(MarvelApiKeyNotFoundException.class);

        this.mockMvc.perform(get("/characters")).andExpect(status().isForbidden());
    }

    @Test
    public void getCharacters_will_return_exception_if_unable_to_process_json() throws Exception {
        List<Long> characterIds = new ArrayList<>();
        characterIds.add(100L);
        characterIds.add(200L);

        when(marvelService.getCharactersIds()).thenThrow(UnableToProcessJsonException.class);

        this.mockMvc.perform(get("/characters")).andExpect(status().isUnprocessableEntity());
    }

    @Test
    public void getCharacters_will_return_exception_if_missing_node() throws Exception {
        List<Long> characterIds = new ArrayList<>();
        characterIds.add(100L);
        characterIds.add(200L);

        when(marvelService.getCharactersIds()).thenThrow(MissingJsonNodeException.class);

        this.mockMvc.perform(get("/characters")).andExpect(status().isUnprocessableEntity());
    }

    @Test
    public void getCharacter_should_return_ok() throws Exception {
        MarvelCharacter marvelCharacter = new MarvelCharacter(100L, "SpiderMan", "Our beloved Spiderman",
                new MarvelCharacter.Thumbnail(
                        "http://i.annihil.us/u/prod/marvel/i/mg/3/20/5232158de5b16", "jpg"));

        when(marvelService.getCharacter(100L)).thenReturn(marvelCharacter);

        ObjectMapper objectMapper = new ObjectMapper();

        this.mockMvc.perform(get("/characters/100")).andExpect(status().isOk())
                .andExpect(content().string(objectMapper.writeValueAsString(marvelCharacter)));
    }

    @Test
    public void getCharacter_will_return_exception_if_no_marvel_api_key() throws Exception {
        MarvelCharacter marvelCharacter = new MarvelCharacter(100L, "SpiderMan", "Our beloved Spiderman",
                new MarvelCharacter.Thumbnail(
                        "http://i.annihil.us/u/prod/marvel/i/mg/3/20/5232158de5b16", "jpg"));

        when(marvelService.getCharacter(100L)).thenThrow(MarvelApiKeyNotFoundException.class);

        this.mockMvc.perform(get("/characters/100")).andExpect(status().isForbidden());
    }

    @Test
    public void getCharacter_will_return_exception_if_unable_to_process_json() throws Exception {
        MarvelCharacter marvelCharacter = new MarvelCharacter(100L, "SpiderMan", "Our beloved Spiderman",
                new MarvelCharacter.Thumbnail(
                        "http://i.annihil.us/u/prod/marvel/i/mg/3/20/5232158de5b16", "jpg"));

        when(marvelService.getCharacter(100L)).thenThrow(UnableToProcessJsonException.class);

        this.mockMvc.perform(get("/characters/100")).andExpect(status().isUnprocessableEntity());
    }

    @Test
    public void getCharacter_will_return_exception_if_missing_node() throws Exception {
        MarvelCharacter marvelCharacter = new MarvelCharacter(100L, "SpiderMan", "Our beloved Spiderman",
                new MarvelCharacter.Thumbnail(
                        "http://i.annihil.us/u/prod/marvel/i/mg/3/20/5232158de5b16", "jpg"));

        when(marvelService.getCharacter(100L)).thenThrow(MissingJsonNodeException.class);

        this.mockMvc.perform(get("/characters/100")).andExpect(status().isUnprocessableEntity());
    }

    @Test
    public void getTranslatedCharacter_should_return_ok() throws Exception {
        MarvelCharacter marvelCharacter = new MarvelCharacter(100L, "SpiderMan", "Unser geliebter Spiderman",
                new MarvelCharacter.Thumbnail(
                        "http://i.annihil.us/u/prod/marvel/i/mg/3/20/5232158de5b16", "jpg"));

        when(marvelService.getTranslatedCharacter(100L, "de")).thenReturn(marvelCharacter);

        ObjectMapper objectMapper = new ObjectMapper();

        this.mockMvc.perform(get("/characters/100?language=de")).andExpect(status().isOk())
                .andExpect(content().string(objectMapper.writeValueAsString(marvelCharacter)));
    }

    @Test
    public void getTranslatedCharacter_will_return_exception_if_no_marvel_api_key() throws Exception {
        MarvelCharacter marvelCharacter = new MarvelCharacter(100L, "SpiderMan", "Unser geliebter Spiderman",
                new MarvelCharacter.Thumbnail(
                        "http://i.annihil.us/u/prod/marvel/i/mg/3/20/5232158de5b16", "jpg"));

        when(marvelService.getTranslatedCharacter(100L, "de")).thenThrow(MarvelApiKeyNotFoundException.class);

        this.mockMvc.perform(get("/characters/100?language=de")).andExpect(status().isForbidden());
    }

    @Test
    public void getTranslatedCharacter_will_return_exception_if_no_yandex_api_key() throws Exception {
        MarvelCharacter marvelCharacter = new MarvelCharacter(100L, "SpiderMan", "Unser geliebter Spiderman",
                new MarvelCharacter.Thumbnail(
                        "http://i.annihil.us/u/prod/marvel/i/mg/3/20/5232158de5b16", "jpg"));

        when(marvelService.getTranslatedCharacter(100L, "de")).thenThrow(YandexApiKeyNotFoundException.class);

        this.mockMvc.perform(get("/characters/100?language=de")).andExpect(status().isForbidden());
    }

    @Test
    public void getTranslatedCharacter_will_return_exception_if_unable_to_process_json() throws Exception {
        MarvelCharacter marvelCharacter = new MarvelCharacter(100L, "SpiderMan", "Unser geliebter Spiderman",
                new MarvelCharacter.Thumbnail(
                        "http://i.annihil.us/u/prod/marvel/i/mg/3/20/5232158de5b16", "jpg"));

        when(marvelService.getTranslatedCharacter(100L, "de")).thenThrow(UnableToProcessJsonException.class);

        this.mockMvc.perform(get("/characters/100?language=de")).andExpect(status().isUnprocessableEntity());
    }

    @Test
    public void getTranslatedCharacter_will_return_exception_if_missing_node() throws Exception {
        MarvelCharacter marvelCharacter = new MarvelCharacter(100L, "SpiderMan", "Unser geliebter Spiderman",
                new MarvelCharacter.Thumbnail(
                        "http://i.annihil.us/u/prod/marvel/i/mg/3/20/5232158de5b16", "jpg"));

        when(marvelService.getTranslatedCharacter(100L, "de")).thenThrow(MissingJsonNodeException.class);

        this.mockMvc.perform(get("/characters/100?language=de")).andExpect(status().isUnprocessableEntity());
    }
}