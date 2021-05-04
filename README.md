# ProductivityTracker

Sovellus mahdollistaa käyttäjän opiskelun seuraamisen ajallisesti. Ohjelma antaa mahdollisuuden luoda tavoiteaikoja opiskelulle per. päivä, per. kurssi. Seuranta tapahtuu ns. "opiskelusessioissa", jonka jälkeen opiskelijalle kerrotaan kuinka suuren osan kyseinen sessio täytti tavoiteajasta.

Valmiissa versiossa sessiot otetaan talteen ja ne lajitellaan tietokantaan päivien, kurssien ja mahdollisesti jopa kellonaikojen suhteen. Tämä mahdollistaa sovelluksen esittää käyttäjälleen tarkempaa *tilastollista* tietoa omasta opiskelusta. Käyttäjälle annetaan myös mahdollisuus asettaa lopuksi kurssille saatu arvosana ja tarkastella sitä, kuinka arvosana heijastuu opiskeltuun aikaan. Kokonaisuudessaan kyseessä on siis statistinen työkalu opiskelijan arkeen.


## Dokumentaatio

[Vaativuusmäärittely](https://github.com/TuuPu/ot-harjoitustyo/blob/master/dokumentaatio/vaativuusmaarittely.md)

[Tuntikirjanpito](https://github.com/TuuPu/ot-harjoitustyo/blob/master/dokumentaatio/tuntikirjanpito.md)

[Käyttöohje](https://github.com/TuuPu/ot-harjoitustyo/blob/master/dokumentaatio/kayttoohje.md)

[Arkkitehtuuri](https://github.com/TuuPu/ot-harjoitustyo/blob/master/dokumentaatio/arkkitehtuuri.md)

## Komentorivitoiminnot

### Suoritus

```
mvn compile exec:java -Dexec.mainClass=productivitytracker.Main
```

### Testi

```
mvn test
```

Testikattavuuden luominen:

```
mvn test jacoco:report
```
## JavaDoc

JavaDoc generoidaan komennolla:

```
mvn javadoc:javadoc
```
JavaDocia voi tarkastelemalla avaamalla selaimella tiedosto target/site/apidocs/index.html

## Suoritettavan jarin generoiminen

Komento

```
mvn package
```

Generoi hakemistoon target suoritettavan jar-tiedoston ProductivityTracker-1.0-SNAPSHOT.jar
Jaria generoitaessa sen avaaminen suoraan target-hakemistosta ei toiminut. Ratkaisu tähän on siirtää jar-tiedosto toiseen hakemistoon ja avata se sieltä. Omassa kansiorakenteessa siirsin sen samaan hakemistoon pom.xml tiedoston kanssa, josta suoritus onnistui.

## Checkstyle

Checkstylen tarkistamiseksi voit suorittaa komennon

```
mvn jxr:jxr checkstyle:checkstyle
```

Generoidun raportin löydät hakemistosta target/site/checkstyle.html

## Releaset

Viidennen viikon releasen löydät [Tästä](https://github.com/TuuPu/ot-harjoitustyo/releases/tag/Viikko5) linkistä.
