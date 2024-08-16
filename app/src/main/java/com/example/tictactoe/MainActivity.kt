package com.example.tictactoe

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableIntState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import com.example.tictactoe.ui.theme.TictactoeTheme


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)




        setContent {
            var isGameStarted: Boolean by remember {
                mutableStateOf(false)
            }
            var boards: Array<Array<String>> by rememberSaveable {
                mutableStateOf(
                    arrayOf()
                );
            }
            var currentPlayer: MutableIntState = remember {
                mutableIntStateOf(0);
            }
            var boardsCoordinate: HashMap<String,String> by rememberSaveable {
                mutableStateOf(hashMapOf())
            };

            fun setCoordinateValue(coordinate:String, value:String){
                boardsCoordinate[coordinate] = value;
            }

            var isGameFinished: Boolean by rememberSaveable {
                mutableStateOf(false)
            }

            boards.forEach { children ->
                children.forEach {
                        coordinate ->
                    setCoordinateValue(coordinate,"")
                }
            }

            var winBox: Array<String> by remember {
                mutableStateOf(arrayOf())
            }

            fun checkWin() {
                var bc = boardsCoordinate;
                if (bc["A1"] == bc["A2"] && bc["A2"] == bc["A3"] && "" !in arrayOf(bc["A1"],bc["A2"],bc["A3"])){
                    winBox = arrayOf("A1","A2","A3");
                    isGameFinished = true;
                } else if (bc["B1"] == bc["B2"] && bc["B2"] == bc["B3"] && "" !in arrayOf(bc["B1"],bc["B2"],bc["B3"])){
                    winBox = arrayOf("B1","B2","B3");
                    isGameFinished = true;
                } else if (bc["C1"] == bc["C2"] && bc["C2"] == bc["C3"] && "" !in arrayOf(bc["C1"],bc["C2"],bc["C3"])){
                    winBox = arrayOf("C1","C2","C3");
                    isGameFinished = true;
                } else if (bc["A1"] == bc["B1"] && bc["B1"] == bc["C1"] && "" !in arrayOf(bc["A1"],bc["B1"],bc["C1"])){
                    winBox = arrayOf("A1","B1","C1");
                    isGameFinished = true;
                } else if (bc["A2"] == bc["B2"] && bc["B2"] == bc["C2"] && "" !in arrayOf(bc["A2"],bc["B2"],bc["C2"])){
                    winBox = arrayOf("A2","B2","C2");
                    isGameFinished = true;
                } else if (bc["A3"] == bc["B3"] && bc["B3"] == bc["C3"] && "" !in arrayOf(bc["A3"],bc["B3"],bc["C3"])){
                    winBox = arrayOf("A3","B3","C3");
                    isGameFinished = true;
                }  else if (bc["A1"] == bc["B2"] && bc["B2"] == bc["C3"] && "" !in arrayOf(bc["A1"],bc["B2"],bc["C3"])){
                    winBox = arrayOf("A1","B2","C3");
                    isGameFinished = true;
                } else if (bc["A3"] == bc["B2"] && bc["B2"] == bc["C1"] && "" !in arrayOf(bc["A3"],bc["B2"],bc["C1"])){
                    winBox = arrayOf("A3","B2","C1");
                    isGameFinished = true;
                }
                else{
                    if("" !in arrayOf(bc["A1"],bc["A2"],bc["A3"],bc["B1"],bc["B2"],bc["B3"],bc["C1"],bc["C2"],bc["C3"])){
                        isGameFinished = true;
                    }
                    winBox = arrayOf();
                }
            }

            fun resetGame(){
                boards = arrayOf();
                isGameFinished=false;
                boards.forEach { children ->
                    children.forEach {
                            coordinate ->
                        setCoordinateValue(coordinate,"")
                    }
                };
                currentPlayer.value = 0;
                winBox = arrayOf();
                isGameStarted = false;
            }

            fun getBottomText(): String {
                if (isGameFinished){
                    return "Game is finished"
                }
                else {
                    return "Player ${currentPlayer.value + 1} is playing. \n\nplease place a \n\n${if (currentPlayer.value == 0) "circle (o)" else "cross (x)"}"
                }
            }

            TictactoeTheme {
                // A surface container using the 'background' color from the theme
             
                    Column(
                        Modifier
                            .fillMaxSize()
                            .background(MaterialTheme.colorScheme.background),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        if (isGameStarted){
                            fun getColor():Color{
                                if (isGameFinished){
                                    return Color.White;
                                }
                                if (currentPlayer.value == 0) return Color.Cyan;
                                else return Color.Yellow
                            }
                            Text(text = getBottomText(), color = getColor(), fontSize = 7.em)
                            Box(Modifier.padding(bottom = 50.dp))
                            boards.forEach { item ->
                                TikTakBox(rows = item, currentPlayer, ::setCoordinateValue, ::checkWin, winBox, isGameFinished)
                            }

                        } else{
                            Text(text = "TikTakToe Game", color = Color.White, fontSize = 7.em)
                            Box(Modifier.padding(bottom = 50.dp))
                            Button(onClick = {
                                isGameStarted = true;
                                boards =  arrayOf(
                                    arrayOf("A1","A2","A3"),
                                    arrayOf("B1","B2","B3"),
                                    arrayOf("C1","C2","C3"));
                            }) {
                                Text(text = "Start The Game")
                            }
                        }

                        if (isGameFinished){
                            fun getFinishedText (): String {
                                if ("o" == boardsCoordinate[winBox[0]]) return "Player 1 is the winner"
                                else if ("x" == boardsCoordinate[winBox[0]]) return "Player 2 is the winner"
                                else return "The result is a draw"
                            }
                            Box(Modifier.padding(bottom = 50.dp))
                            Text(text = "${getFinishedText()}", color = Color.White, fontSize = 7.em)
                            Box(Modifier.padding(bottom = 50.dp))
                            Button(onClick = {
                               resetGame();
                            }) {
                                Text(text = "Go Back")
                            }
                        }
                    }
            }
        }
    }
}

@Composable
fun TikTakBox(rows:Array<String>,
              currentPlayer:MutableIntState,
              setCoordinateValue:(coordinate:String, value:String)->Unit,
              checkWin: ()->Unit,
              winBox:Array<String>,
              isGameFinished: Boolean
) {
    Row {
        rows.forEach { coordinate ->
            var choose: String by remember { mutableStateOf("") }
            var disabled: Boolean by remember{ mutableStateOf(false) }
            fun backGroundColor (): Color {
                if (coordinate in winBox)
                    return Color.Green;
                else{
                    if (choose === "o"){
                        return Color.Cyan
                    } else if (choose === "x"){
                        return Color.Yellow
                    } else{
                        return Color.White
                    }
                }
            }
            Column(
                modifier = Modifier
                    .padding(end = 20.dp, top = 20.dp, bottom = 20.dp)
                    .background(backGroundColor())
                    .width(100.dp)
                    .height(100.dp)
                    .clickable {
                        if (!disabled && !isGameFinished) {
                            disabled = true;
                            var player = currentPlayer.value;
                            choose = if (player == 0) "o" else "x";
                            currentPlayer.intValue = if (player == 0) 1 else 0;
                            setCoordinateValue(coordinate, choose);
                            checkWin();
                        }

                    },
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center

            ){
                Text(text = choose, fontSize = 10.em, textAlign = TextAlign.Center)
            }
        }

    }

}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    TictactoeTheme {

    }
}