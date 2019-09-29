package com.bol.assignment.kalahgame.resource;

import com.bol.assignment.kalahgame.model.Board;
import com.bol.assignment.kalahgame.model.Kalah;
import com.bol.assignment.kalahgame.model.MoveResponse;
import com.bol.assignment.kalahgame.service.KalahService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class KalahControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private KalahService kalahSrv;

    @Test
    public void startTest() throws Exception {
        when(kalahSrv.initialize()).thenReturn(new Kalah());
        mockMvc.perform(post("/games"))
                .andDo(print())
                .andExpect(status().isCreated());
    }

    @Test
    public void moveTestHappyFlow() throws Exception {
        when(kalahSrv.move("a4f9a6da-2357-42bd-9f7c-d647bd28c2c0", 1)).thenReturn(getMockResponse());
        mockMvc.perform(put("/games/a4f9a6da-2357-42bd-9f7c-d647bd28c2c0/pits/1"))
                .andDo(print()).andExpect(status().isOk());
    }

    @Test
    public void moveTestWhenInvalidPitId() throws Exception {
        when(kalahSrv.move("a4f9a6da-2357-42bd-9f7c-d647bd28c2c0", 19)).thenReturn(getMockBadRequest());
        mockMvc.perform(put("/games/a4f9a6da-2357-42bd-9f7c-d647bd28c2c0/pits/19"))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    private MoveResponse getMockBadRequest() {
        return new MoveResponse(true, "Invalid pit Id");
    }

    @Test
    public void moveTestWhenPlayerHome() throws Exception {
        when(kalahSrv.move("a4f9a6da-2357-42bd-9f7c-d647bd28c2c0", 7)).thenReturn(getMockBadRequest());
        mockMvc.perform(put("/games/a4f9a6da-2357-42bd-9f7c-d647bd28c2c0/pits/7"))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    private MoveResponse getMockResponse() {
        Kalah kalah = new Kalah();
        kalah.setId("a4f9a6da-2357-42bd-9f7c-d647bd28c2c0");
        kalah.setBoard(new Board());
        return new MoveResponse(false, null, kalah);
    }
}
