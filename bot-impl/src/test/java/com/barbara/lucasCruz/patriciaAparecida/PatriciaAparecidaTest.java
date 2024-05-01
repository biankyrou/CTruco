package com.barbara.lucasCruz.patriciaAparecida;

import com.bueno.spi.model.CardRank;
import com.bueno.spi.model.CardSuit;
import com.bueno.spi.model.GameIntel;
import com.bueno.spi.model.TrucoCard;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;
import java.util.Random;

import static com.bueno.spi.model.CardRank.*;
import static com.bueno.spi.model.CardRank.FIVE;
import static com.bueno.spi.model.CardSuit.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

class PatriciaAparecidaTest {

    private PatriciaAparecida patricia;
    private GameIntel.StepBuilder stepBuilder;

    @BeforeEach
    void setUp() {
        patricia = new PatriciaAparecida();
    }

    @Nested
    @DisplayName("Check the variables to calculate the probability")
    class checkVariables {

        @Nested
        @DisplayName("Number of remainder cards")
        class remainderCards{

            @Test
            @DisplayName("Start Of the Game")
            public void startOfTheGame() {

                List<TrucoCard> botCards = List.of(
                        generateRandomCardToPlay(),
                        generateRandomCardToPlay(),
                        generateRandomCardToPlay());
                TrucoCard vira = generateRandomCardToPlay();
                List<TrucoCard> openCards = List.of(vira);

                stepBuilder = GameIntel.StepBuilder.with().
                        gameInfo(Collections.EMPTY_LIST, openCards, vira, 0).
                        botInfo(botCards, 0).
                        opponentScore(0);

                assertEquals(patricia.getNumberOfRemainderCards(stepBuilder.build()), 36);
            }

            @Test
            @DisplayName("Last Round")
            public void lastRound () {

                List<TrucoCard> botCards = List.of(generateRandomCardToPlay());
                TrucoCard vira = generateRandomCardToPlay();
                List<TrucoCard> openCards = List.of(
                        vira,
                        generateRandomCardToPlay(),
                        generateRandomCardToPlay(),
                        generateRandomCardToPlay(),
                        generateRandomCardToPlay(),
                        generateRandomCardToPlay());
                List<GameIntel.RoundResult> roundResults = List.of(GameIntel.RoundResult.WON,
                                                                GameIntel.RoundResult.LOST);

                stepBuilder = GameIntel.StepBuilder.with().
                        gameInfo(roundResults, openCards, vira, 0).
                        botInfo(botCards, 0).
                        opponentScore(0);

                assertEquals(patricia.getNumberOfRemainderCards(stepBuilder.build()), 33);
            }

        }

    }


        public GameIntel.RoundResult generateRandomRoundResult() {
        GameIntel.RoundResult[] values = GameIntel.RoundResult.values();

        Random random = new Random();
        int index = random.nextInt(values.length);

        return values[index];
    }

    public CardRank generateRandomCardRank() {
        CardRank[] values = CardRank.values();
        Random random = new Random();
        int index = random.nextInt(values.length);
        return values[index];
    }

    public CardSuit generateRandomCardSuit() {
        CardSuit[] values = CardSuit.values();
        Random random = new Random();
        int index = random.nextInt(values.length);

        while (values[index] == CardSuit.HIDDEN) {
            index = random.nextInt(values.length);
        }

        return values[index];
    }

    public TrucoCard generateRandomCard(){ //assegura que CardRank.HIDDEN só receba CardSuit.HIDDEN
        CardRank cardRank = generateRandomCardRank();
        if(cardRank == CardRank.HIDDEN){
            return TrucoCard.of(cardRank,CardSuit.HIDDEN);
        }
        return TrucoCard.of(cardRank,generateRandomCardSuit());
    }

    public TrucoCard generateRandomCardToPlay(){ //sem cartas viradas (hidden)
        TrucoCard cardToPlay = generateRandomCard();
        while (cardToPlay.getSuit() == CardSuit.HIDDEN) {
            cardToPlay = generateRandomCard();
        }
        return cardToPlay;
    }

}