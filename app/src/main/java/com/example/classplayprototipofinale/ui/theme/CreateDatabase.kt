package com.example.classplayprototipofinale.ui.theme

import com.example.classplayprototipofinale.R
import com.example.classplayprototipofinale.models.Cosplay
import com.example.classplayprototipofinale.models.Review
import com.example.classplayprototipofinale.models.ToDo
import com.example.classplayprototipofinale.models.TutorialStep
import com.example.classplayprototipofinale.models.Users
import com.example.classplayprototipofinale.screens.AppIcons
import com.example.classplayprototipofinale.screens.LinkType
import com.google.firebase.database.DatabaseReference
import com.google.firebase.storage.StorageReference
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


class CreateDatabase (){
    fun cosplay(cpDB: DatabaseReference, cosplayImgsSRef: StorageReference){

        val newCosplay = Cosplay("Goku Super Sayan")
        newCosplay.username = "DragonMaker"
        newCosplay.date = "01/02/2024"

        val images = mutableMapOf<String, String>(
            "81ab8db6-9162-4c59-8f11-c9d2bb0ceca0" to "https://firebasestorage.googleapis.com/v0/b/classplaydb-f47f8.appspot.com/o/CosplaysImgs%2F81ab8db6-9162-4c59-8f11-c9d2bb0ceca0.jpeg?alt=media&token=3117c128-643b-44cf-9abe-a51bc4316062",
            "b227e405-51ea-4972-a6e6-16e17fc2d425" to "https://firebasestorage.googleapis.com/v0/b/classplaydb-f47f8.appspot.com/o/CosplaysImgs%2Fb227e405-51ea-4972-a6e6-16e17fc2d425.jpeg?alt=media&token=65d91f10-b25f-4062-9dd3-462a03ffac2d"
        )
        newCosplay.imgUrls = images
        newCosplay.description = "Segui questi passi per diventare il leggendario Sayan: dall’arte del taglio e cucito per il giubbotto arancione perfetto, alle tecniche di parrucca per ottenere la chioma di Super Sayan. Sblocca il tuo potenziale cosplay e portalo al livello successivo con questo tutorial dettagliato."
        newCosplay.avgReviews = "3.5"

        val review1 = Review("PotterHead06")
        review1.vote = 3

        val review2 = Review("MarvelFanBoy_XD")
        review2.vote = 4

        newCosplay.reviews = mutableMapOf<String, Review>("PotterHead06" to review1, "MarvelFanBoy_XD" to review2)

        newCosplay.tags = mutableListOf("goku", "super sayan", "dragonball", "Manga", "CartoniAnimati", "anime")

        newCosplay.time = mutableMapOf("mesi" to 3, "settimane" to 2)

        newCosplay.material = "gomma piuma, lenzuolo arancione, stoffa nera, pittura gialla"

        val newTutorial = mutableMapOf<String, TutorialStep>()

        val tutorialStep1 = TutorialStep(0)
        tutorialStep1.icon = AppIcons.WIG.url
        tutorialStep1.componentName = "Parrucca"
        tutorialStep1.link = "https://www.youtube.com/watch?v=G0D2rW0FLZg"
        tutorialStep1.description = "Da una semplice parrucca bionda si può ottenere una ottima capigliatura alla goku super sayan"
        tutorialStep1.linkType = LinkType.LINK.txt
        tutorialStep1.realAppLink = "https://www.youtube.com/watch?v=G0D2rW0FLZg"

        val tutorialStep2 = TutorialStep(1)
        tutorialStep2.icon = AppIcons.TSHIRT.url
        tutorialStep2.componentName = "Casacca"
        tutorialStep2.link = "Goku::Casacca"
        tutorialStep2.description = "Ho creato la casacca di goku guardando questo tutorial, totalmente fatta a mano!"
        tutorialStep2.linkType = LinkType.LINK.txt
        tutorialStep2.realAppLink = "https://www.youtube.com/watch?v=50h1yB6WFqo"

        val tutorialStep3 = TutorialStep(2)
        tutorialStep3.icon = AppIcons.TROUSERS.url
        tutorialStep3.componentName = "Pantaloni"
        tutorialStep3.link = ""
        tutorialStep3.description = "Ho acquistato i pantaloni già fatti da internet"
        tutorialStep3.linkType = LinkType.NONE.txt
        tutorialStep3.realAppLink = ""

        newTutorial["s0"] = tutorialStep1
        newTutorial["s1"] = tutorialStep2
        newTutorial["s2"] = tutorialStep3

        newCosplay.tutorial = newTutorial

        cpDB.child("Goku Super Sayan").setValue(newCosplay)



    }

    fun todo(uDB: DatabaseReference, cosplayImgsSRef: StorageReference, user: Users){
        var userUpdate = user

        userUpdate.yourToDo = mutableMapOf<String, ToDo>("Prova" to ToDo("Prova"))

        uDB.child(user.username!!).setValue(userUpdate)
    }
}