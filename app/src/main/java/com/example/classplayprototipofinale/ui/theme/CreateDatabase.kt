package com.example.classplayprototipofinale.ui.theme

import com.example.classplayprototipofinale.models.Cosplay
import com.example.classplayprototipofinale.models.Review
import com.example.classplayprototipofinale.models.ToDo
import com.example.classplayprototipofinale.models.TutorialStep
import com.example.classplayprototipofinale.models.Users
import com.example.classplayprototipofinale.screens.AppIcons
import com.example.classplayprototipofinale.screens.LinkType
import com.google.firebase.database.DatabaseReference
import com.google.firebase.storage.StorageReference


class CreateDatabase (){
    fun cosplay(cpDB: DatabaseReference, cosplayImgsSRef: StorageReference){

        val newCosplay = Cosplay("Walter White")
        newCosplay.username = "ProCosp_89"
        newCosplay.date = "18/11/2023"

        val images = mutableMapOf<String, String>(
            "c789a440-36b3-4138-b62f-ee4c249c9230" to "https://firebasestorage.googleapis.com/v0/b/classplaydb-f47f8.appspot.com/o/CosplaysImgs%2Fc789a440-36b3-4138-b62f-ee4c249c9230.jpg?alt=media&token=33a7dfac-d54d-4f7c-adf6-d64b3c378b59",
            "3786d726-a81c-4ce8-9438-cee30884c96b" to "https://firebasestorage.googleapis.com/v0/b/classplaydb-f47f8.appspot.com/o/CosplaysImgs%2F3786d726-a81c-4ce8-9438-cee30884c96b.jpg?alt=media&token=ea5d6592-b2c6-45e2-afd3-50505529160f"
        )
        newCosplay.imgUrls = images
        newCosplay.description = "Breaking bad, breaking rules. Indossando il mio cosplay di Walter White, mi trasformo nel genio chimico di Albuquerque. Un pizzetto nero, una barba incolta e il potere del blu. Preparati per un viaggio nel mondo dell'alta chimica e della pericolosa ambizione. Scommetti sulla mia formula per il successo?"
        newCosplay.avgReviews = "2.5"

        val review1 = Review("MethLover98")
        review1.vote = 3

        val review2 = Review("Pinkman_wow")
        review2.vote = 2

        newCosplay.reviews = mutableMapOf<String, Review>("MethLover98" to review1, "Pinkman_wow" to review2)

        newCosplay.tags = mutableListOf("Walter White", "Breaking Bad", "serie tv", "Metanfetamina", "netflix", "science", "tv")

        newCosplay.time = mutableMapOf("settimane" to 3)

        newCosplay.material = "tuta intera di plastica gialla, guanti in lattice azzurri, sale da cucina, tempera blu/azzurra, occhiali tondi, maschera antigas, trucco nero"

        val newTutorial = mutableMapOf<String, TutorialStep>()

        val tutorialStep1 = TutorialStep(0)
        tutorialStep1.icon = AppIcons.TSHIRT.url
        tutorialStep1.componentName = "Costume"
        tutorialStep1.link = "https://www.amazon.com/TigerTough-Chemical-Protection-Coveralls-Men/dp/B0BTKG7TMW/ref=sr_1_1_sspa?crid=24X27BAYL88AP&keywords=yellow+coveralls+for+men&qid=1706803799&sprefix=yellow+coveralls+for+men%2Caps%2C344&sr=8-1-spons&sp_csd=d2lkZ2V0TmFtZT1zcF9hdGY&psc=1"
        tutorialStep1.description = "tuta comprata online"
        tutorialStep1.linkType = LinkType.LINK.txt
        tutorialStep1.realAppLink = "https://www.amazon.com/TigerTough-Chemical-Protection-Coveralls-Men/dp/B0BTKG7TMW/ref=sr_1_1_sspa?crid=24X27BAYL88AP&keywords=yellow+coveralls+for+men&qid=1706803799&sprefix=yellow+coveralls+for+men%2Caps%2C344&sr=8-1-spons&sp_csd=d2lkZ2V0TmFtZT1zcF9hdGY&psc=1"

        val tutorialStep2 = TutorialStep(1)
        tutorialStep2.icon = AppIcons.WIG.url
        tutorialStep2.componentName = "Trucco"
        tutorialStep2.link = "https://www.youtube.com/watch?v=AeNltFDJS54"
        tutorialStep2.description = "trucco ispirato a questo video youtube"
        tutorialStep2.linkType = LinkType.LINK.txt
        tutorialStep2.realAppLink = "https://www.youtube.com/watch?v=AeNltFDJS54"

        newTutorial["s0"] = tutorialStep1
        newTutorial["s1"] = tutorialStep2

        newCosplay.tutorial = newTutorial

        cpDB.child(newCosplay.cosplayName!!).setValue(newCosplay)


    }

    fun todo(uDB: DatabaseReference, cosplayImgsSRef: StorageReference, user: Users){
        val userUpdate = user

        userUpdate.yourToDo = mutableMapOf<String, ToDo>("Prova" to ToDo("Prova"))

        uDB.child(user.username!!).setValue(userUpdate)
    }

    fun user(uDB: DatabaseReference) {
        var newUser = Users("BatFan_96")
        newUser.bio = "Io sono la notte, io sono vendetta, io sono Batman"
        newUser.profileImgUrl = "https://firebasestorage.googleapis.com/v0/b/classplaydb-f47f8.appspot.com/o/CosplaysImgs%2F700309f0-e95a-42cf-9f86-69218049ed9a.jpeg?alt=media&token=69c6fb3a-7754-4bf0-91f8-8e6a42aa66d9"
        newUser.emailAddress = "batfan_96@gmail.com"
        newUser.phoneNumber = "3425679803"

        uDB.child(newUser.username!!).setValue(newUser)

        newUser = Users("Virtual_Robb")
        newUser.bio = "Cosplayer professionista. Vieni a vedermi al Torino Comics!"
        newUser.profileImgUrl = "https://firebasestorage.googleapis.com/v0/b/classplaydb-f47f8.appspot.com/o/CosplaysImgs%2F4688e706-2735-477e-b649-325efb902107.jpeg?alt=media&token=3cc250f0-59da-45d5-8904-26ce1921861f"
        newUser.emailAddress = "virtual_robb@hotmail.com"
        newUser.phoneNumber = "3456129840"

        uDB.child(newUser.username!!).setValue(newUser)

        newUser = Users("Lazh_TheGreat")
        newUser.bio = "Why So Seriuous? Faccio i migliori cosplay a tema cinecomics!"
        newUser.profileImgUrl = "https://firebasestorage.googleapis.com/v0/b/classplaydb-f47f8.appspot.com/o/CosplaysImgs%2Faa549e88-0944-403b-bcf2-c065d9986404.jpeg?alt=media&token=74b25e47-cfb0-4ed6-ab79-fdd75e38be51"
        newUser.emailAddress = "Lazh_Joker@outlook.com"
        newUser.phoneNumber = "7906344663"

        uDB.child(newUser.username!!).setValue(newUser)

        newUser = Users("Swagger_01")
        newUser.bio = "Ciao sono Swagger, sono il fan numero 1 di one piece al mondo!"
        newUser.profileImgUrl = "https://firebasestorage.googleapis.com/v0/b/classplaydb-f47f8.appspot.com/o/CosplaysImgs%2Facef2bb5-46a2-4024-9890-b9161c432eaf.jpg?alt=media&token=d4101c4b-694a-41d7-ad06-29349bff840e"
        newUser.emailAddress = "superSwagger01@libero.it"
        newUser.phoneNumber = "3561879012"

        uDB.child(newUser.username!!).setValue(newUser)

        newUser = Users("ProCosp_89")
        newUser.bio = "My Baby Bluuuuuuuu. Now, say my name!"
        newUser.profileImgUrl = "https://firebasestorage.googleapis.com/v0/b/classplaydb-f47f8.appspot.com/o/CosplaysImgs%2F3786d726-a81c-4ce8-9438-cee30884c96b.jpg?alt=media&token=ea5d6592-b2c6-45e2-afd3-50505529160f"
        newUser.emailAddress = "walterCOSP_89@gmail.com"
        newUser.phoneNumber = "3456789012"

        uDB.child(newUser.username!!).setValue(newUser)

        newUser = Users("Lucy_rocket03")
        newUser.bio = "Come with me into the CosplayVerse!"
        newUser.profileImgUrl = "https://firebasestorage.googleapis.com/v0/b/classplaydb-f47f8.appspot.com/o/CosplaysImgs%2F28337d4f-7e5e-4077-844e-15b9490691c1.jpg?alt=media&token=960ff582-4e04-45b4-8b75-7b3bd9b5687e"
        newUser.emailAddress = "rocketToTheMoon_03@gmail.com"
        newUser.phoneNumber = "1535789563"

        uDB.child(newUser.username!!).setValue(newUser)
    }
}