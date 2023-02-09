package com.ddd.pollpoll.util

import java.util.*

val PERSONALITIES = listOf(
    "정직한",
    "재밌는",
    "느긋한",
    "사려깊은",
    "끈질긴",
    "친절한",
    "유능한",
    "멋쟁이",
    "침착한",
    "엄격한",
    "고집센",
    "순진한",
    "겸손한",
    "용감한",
    "다정한",
    "긍정적인",
    "대담한",
    "명랑한",
    "부지런한",
    "귀여운",
    "상냥한"
)
val ANIMALS = listOf(
    "쥐",
    "소",
    "호랑이",
    "토끼",
    "용",
    "뱀",
    "말",
    "양",
    "원숭이",
    "닭",
    "강아지",
    "돼지",
    "고양이",
    "다람쥐",
    "사자",
    "고래",
    "기린",
    "병아리",
    "독수리",
    "코끼리",
    "치타",
)

fun getNickname(): String {
    val personalityIndex = Random().nextInt(PERSONALITIES.size)
    val animalIndex = Random().nextInt(ANIMALS.size)
    return "${PERSONALITIES[personalityIndex]}${ANIMALS[animalIndex]}"
}
