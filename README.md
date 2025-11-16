<p align="center"><br>
<a href="https://github.com/Lolo280374/OrpheusTotheMoon"><img src="https://hackatime-badge.hackclub.com/U09CBF0DS4F/OrpheusTotheMoon"></a>
[![automate APK builds](https://github.com/Lolo280374/OrpheusTotheMoon/actions/workflows/build.yml/badge.svg?branch=master)](https://github.com/Lolo280374/OrpheusTotheMoon/actions/workflows/build.yml)
<a href="https://makeapullrequest.com"><img src="https://img.shields.io/badge/PRs-welcome-brightgreen.svg"></a>
<a href="https://devrant.com/rants/4149950/i-fucking-hate-mobile-development-i-already-manage-the-data-devops-infra-and-mos"><img src="https://img.shields.io/badge/android-supported-green"></a>
<a href="https://devrant.com/rants/4149950/i-fucking-hate-mobile-development-i-already-manage-the-data-devops-infra-and-mos"><img src="https://img.shields.io/badge/android 9-and above-white"></a>
<br></p>

<h3 align="center">
using your phone's gravity sensors (gyro), help orpheus and heidi both reach the moon and start an amazing adventure!
</h3>

<h1 align="center">
showcase (screenshot/gameplay)
</h1>

<img width="1201" height="1080" alt="screenshots-orpheus" src="https://github.com/user-attachments/assets/9148868b-4863-402b-bc48-b51facc4a66a" />

[![showcase_screenrec](https://github.com/user-attachments/assets/fab83dbd-b901-4fd9-a552-f3a95a3465ad)](https://github.com/user-attachments/assets/91679403-2f21-405e-874f-601dd4f1a58c)

you can click on the VLC preview to open the actual video in your browser, if your client does not support the GitHub video embed.

> [!NOTE]  
> i'm aware the game has little to no runtime, and sorry in advance, but this is more an emphasis on the flying feature where you have to fly to the moon yourself with physical controls! make sure to try it out on your phone! (if you have an Android...)

## table of contents

- [about](#about)
- [compatibility](#compatibility)
- [installation](#installation)
- [how to play](#how-to-play)
- [store items](#store-items)
- [contributing](#contributing)
- [reporting issues](#reporting-issues)
- [credits](#credits)
- [license](#license)

## about
there's not much thought behind this game. I always wanted to make something with Android, especially using hardware APIs to make a physical input game. but now I understand why I never did. I hate Kotlin, and I hate Android Studio. but here's the game!

despite that, coding in Kotlin made me learn a lot, truely. i'm quite happy of how the game turned out, even tho it dosen't have that many gametime and it's not really that fun, but it's a cool tech demo of the phone's sensors!

thanks for being here!

## compatibility
this "game" is compatible on any device running at minimum Android 9 or above (Android 9 corresponding to API 28). you will also need physical sensors on your phone for the game experience to correctly run: you need to shake the phone up and down to fly up in the air.

install the game, and you'll immediately tell if they work or not!

## installation
to install, you can either head to the [releases](https://github.com/Lolo280374/OrpheusTotheMoon/releases) or the [GitHub Actions](https://github.com/Lolo280374/OrpheusTotheMoon/actions) builds, grab the latest APK avalaible, and install it on your Android (9+) device!

> [!WARNING]  
> you may get a Play Protect alert, and that is normal due to the fact that this app has little to no installs, and that I'm not wishing to pay for a Google Play dev acc! you can review the code if you wish, but such alerts are normal.

## how to play
this game is very simple to play! if you've ever played soda shake on Wii Party, or anything similar to that (I think the Switch had a similar title but I forgot the name entirely), well that's exactly the same logic! 

move to the fly page, and shake your phone up to fly into the air! once you reach the moon, you're there, and you get coins!

these coins can allow you to buy cosmetics, and that's it! yep sorry! but at least you'll have made orpheus and heidi reach the moon! good job to you.

## store items
there are only 3 items in this store, being purely cosmetic! they are inspired from the Siege store's page, but edited to fit with the flying Orpheus from the game's fly page:
they consist of the following:

- orpheus cowboy hat,
- orpheus sailor hat,
- satchel for orpheus.

that's it!

## contributing

> [!IMPORTANT]  
> Android Studio is highly recommanded as it is the easiest way to clone and start developing right away from my source. install it and you can get going right after cloning!

to contribute, you can simply git clone this repository, and open it within Android Studio (or open the MainActivity Kotlin file in any IDE of your choice):

```sh
git clone https://github.com/Lolo280374/OrpheusTotheMoon.git
cd OrpheusTotheMoon
and either open it in Android Studio or edit the MainActivity.kt file:
nano app/src/main/java/tech/lolodotzip/siegethemoon/MainActivity.kt
```

you may then request your modifications via a PR.

## reporting issues
this is a commnuity project, and your help is very much appreciated! if you notice anything wrong during your usage of this software, please report it to the [GitHub issues tracker](https://github.com/Lolo280374/OrpheusTotheMoon/issues/)!

## credits
many thanks to these, who without them, the project would probably not have been the same, or looked the same at all!
- [Android Developers](https://developer.android.com/) - helped me understand the best how android APIs work and how to make stuff
- [a Material 3 expressive demo](https://github.com/ocegik/Material3Expressive/) - helped me understand how to add Material You to my app!
- [Native Mobile Bits](https://www.youtube.com/watch?v=dC-J0t8fyuc) - same thing as the Material 3 expressive demo, but it helped me put it in practice with actual demos!

and probably some others I forgotten.. sorry in advance, but thanks for being here!

## license
this project is licensed under the MIT License which you may check [here](https://github.com/Lolo280374/OrpheusTotheMoon/blob/master/LICENSE/).
<br>if you have any questions about this project or inquieries, please reach me [at lolodotzip@hackclub.app](mailto:lolodotzip@hackclub.app).
