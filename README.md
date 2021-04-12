# ProductivityTracker

Sovellus mahdollistaa käyttäjän opiskelun seuraamisen ajallisesti. Ohjelma antaa mahdollisuuden luoda tavoiteaikoja opiskelulle per. päivä, per. kurssi. Seuranta tapahtuu ns. "opiskelusessioissa", jonka jälkeen opiskelijalle kerrotaan kuinka suuren osan kyseinen sessio täytti tavoiteajasta.

Valmiissa versiossa sessiot otetaan talteen ja ne lajitellaan tietokantaan päivien, kurssien ja mahdollisesti jopa kellonaikojen suhteen. Tämä mahdollistaa sovelluksen esittää käyttäjälleen tarkempaa *tilastollista* tietoa omasta opiskelusta. Käyttäjälle annetaan myös mahdollisuus asettaa lopuksi kurssille saatu arvosana ja tarkastella sitä, kuinka arvosana heijastuu opiskeltuun aikaan. Kokonaisuudessaan kyseessä on siis statistinen työkalu opiskelijan arkeen.


## Dokumentaatio

[Vaativuusmäärittely](https://github.com/TuuPu/ot-harjoitustyo/blob/master/laskarit/dokumentaatio/vaativuusmaarittely.md)

[Tuntikirjanpito](https://github.com/TuuPu/ot-harjoitustyo/blob/master/laskarit/dokumentaatio/tuntikirjanpito.md)

[Käyttöohje](https://github.com/TuuPu/ot-harjoitustyo/blob/master/laskarit/dokumentaatio/kayttoohje.md)

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
