import java.util.logging.LogManager;
import java.util.logging.Logger;

/**
 *
 * @author saman
 */

 public class Codedoku {
    public static void main(String... args) {
Clerk.view();
try {
Thread.sleep(5000);
}
catch (InterruptedException e) {
System.out.println("IDK");
}

 Clerk.markdown("""

 # **PROGRAMMIERPROJEKT WiSe24/25** 

Saman Aghajani Lazarjani <saman.aghajani.lazarjani@mni.thm.de>

## Inhaltverzeichnis

* [Einleitung](##-einleitung)
* [Überblick über den Code](##-überblick-über-den-code)
	* [Weltkarte](###-die-weltkarte)
		* [Datenstruktur zur Speicherung von Karten](####-datenstruktur-zur-speicherung-von-karten)
		* [Laborynthgenerierung](####-laborynthgenerierung)
		* [Zimmerplatzierung](####-Zimmerplatzierung)
	* [Entitäten](###-entitäten)
		* [die Entitätskomponente](####-die-entitätskomponente)
	* [Inventar bzw. Objektsystem](###-inventar-bzw.-objektsystem)
	* [Künstliche Intelligenz](###-ki)
		* [Idee des Bewertungssystems](####-idee-des-bewertungssystem)
		* [umgesetzte Beispiele](####-umgesetzte-beispiele)
	* [Zeitschritt in der Welt](###-zeitschritt-in-der-welt)
	* [der/die Held*in](###-held*in)
* [Szenarien](#-szenarien)
    * [Szenario 1](##-szenario-1)
    * [Szenario 2](##-szenario-2)
    * [Szenario 3](##-szenario-3)
    * [Szenario 4](##-szenario-4)
        * [der Sidewinder](###-der-sidewinder)
        * [der Backtracker](###-der-backtracker)
        * [der Aldous-Broder](###-der-aldous-broder-bzw.-drunkards-walk)
    * [Szenario 5](##-szenario-5)
* [Ausblick](#-ausblick)
    * [Eine kleine Welt mit einer Rattenfamilie](###-eine-kleine-welt-mit-rattenfamilie)

## Einleitung

**Metro** heißt das gestrebte Programmierprojekt im WiSe24/25, bei dem der Autor versucht hat, den Grundbaustein eines [Rogue-Like](https://de.wikipedia.org/wiki/Rogue-like)  zu hinterlegen.  Die Kernidee des Spiels ließ sich von dem [gleichnamigen Roman](https://de.wikipedia.org/wiki/Metro_2033_(Roman)) von dem russischen Schriftsteller [Dmitri Alexejewitsch Gluchowski](https://de.wikipedia.org/wiki/Dmitri_Alexejewitsch_Gluchowski) inspirieren.  Obwohl es am Ende ja kein vollständiges Spiel entwickelt wurde, hat der Autor trotzdem ein flexibles System zugrunde gelegt, auf dem es leicht aufzubauen.  


Was nun folgt ist eine ausführliche Beschreibung der 5 Szenarien und einen Überblick über die Projektstruktur, etwa wie die Designentscheidungen getroffen worden sind, damit Sie, der/die liebe Leser*in, sich leichter in verschiedenen zurechtDateien  finden können.
In Fällen, wo die gelieferten Szenarien von dem Funktionsversprechen abweichen, folgt eine Begründung.

## Überblick über den Code

Der Code von dem Projekt befindet sich unter "src".  Zum Aus- bzw. Einlesen von JSON-Dateien habe ich [diese Implementierung](https://github.com/stleary/JSON-java) von der Java-API-Spezifikation eingesetzt.

Außerdem enthält das Projekt alles, was er zum Laufen braucht.  Die Datei "Codedoku.java" ist zur Generierung dieser Dokumentation.

### Struktur

Die Welt besteht im Kern aus folgenden Klassen:

* **BSPDungeon**. verwaltet das Labyrinth und Zimmer, außerdem den Zimmerplatzierung-Algorithmus
* **DungeonEntity**. Die Hauptklasse aller im Spielfeld verfügbaren Objekten.  Dieser Bereich streckt sich von Türen bis hin zu allen Lebwesen.
* **DungeonComponent**. Diese Klasse ist ein Grundgerüst für verschiedene Verhaltensimplementierungen, die eine Entität während dessen Lebzeit ausübt.  
    Etwa wie mit anderen Entitäten interagieren, andere Objekte angreifen oder einfach ein Objekt aus dem Inventar zur Hand nehmen.
    Unterklassen sind als "DungeonComponent" bzw. "DungeonAbility" vorgekennzeichnet.
* **DungeonInventory**.  Verwaltet das Inventar einer Entität.  Es merkt die erlaubte Anzahl an Objekten, die sich tragen lassen.
* **DungeonEntityManager**.  Wird verwendet, um aus bzw. in JSON-Dateien Informationen einzulesen bzw.  hinterlegen.
* **DungeonAI**.  Ist für die künstliche Intelligenz zuständig.  Es basiert sich auf dem [Utility-Modell](https://en.wikipedia.org/wiki/Utility_system).   
    Mehr dazu in dem zugehörigen Abschnitt.
* **World**.  Die Kernklasse der Welt. Es ist für die Registrierung  der Entitäten, die Spielschleife (Engl. Gameloop), Kreatur- bzw. Objektanlegung und vieles mehr verantwortlich.  Es bietet im Prinzip eine Schnittstelle für die anderen Klassen, sich mit anderen Entitäten zu kommunizieren.

In den darauffolgenden Teilen versuche ich,  die Aufgaben jedes Komponents ausführlich zu erklären.

### die Weltkarte

Die Weltkarte setzt sich aus einem Graphen und Raumen zusammen.  Der Hintergedanke ist, dass jede Entität  - außer Gegenständen - eine Zelle (Knoten) belegen darf.  Da die Modellierung über einen Graphen abläuft, ist es leicht, darauf Graphenalgorithmen zu verwenden, um daraus  zusammenhängende Graphen zu bauen.  Das dient meiner Vorstellung von unter der Erde liegenden Tunnelln und Zugstationen- etwa  wie die Umgebung im Roman.
Es sind fünf Klassen dafür errichtet:

    
#### Datenstruktur zur Speicherung von Maps

* **Cell**
    Eine Zelle - Graphenknote - besitzt eine Hashtabelle zu den benachbarten Zellen, einen Punkt auf der Karte und den Geländentyp.
    Die Entscheidung, Hashtabelle zur Speicherung von Nachbarn anzuwenden, liegt darin, dass der Schreiber den Gedanken hat, später dann  Richtungen hinzufügen, die nicht notwendigerweise aus den vier Himmelsrichtungen stammen.  Angenommen gäbe es ein Teleportsystem.  Dabei ist es behilflich, eine Zelle mit der anderen über eine neue Richtung zu verbinden - Z.B. Teleport, die dann überschritten werden kann, wenn die Entität dazu fähig ist.
    Außerdem ist es leichter, anhand von Richtungen die verbundenen Nachbarn zu ermitteln.
* **Grid**
    Der Grid ist die Lagerung für die Zellen.  Er bietet dazu auch verschiedene Methoden, um herauszufinden, ob zwei gegebene Punkte verbunden sind, wie viele Nachbarn einer bestimmten Zelle hat usw.
** *Room**
    Die Objekte dieser Klasse vertreten ein Raum.  Ein Raum ist der Bereich, worin alle Punkte verbunden sind, und es stehen Wände an jeder Seite.
    Zur Zeit sind leider nur Viereckräume erstellbar, aber der Authr hat vor, später mehrere komplexe geographische Formen zu implementieren, etwa wie die Kreise.

#### Laborynthgenerierung

Die Klasse **MazeGenerator** stellt die Schnittstelle für die Erstellung eines Labyrinths.  Verschiedene Implementierungen (Sidewinder, Backtracker, Hunt-and-Kill, Aldous-Broder A.K.A. Drunkards Walk) stehen zur Auswahl.  Der dazugehörige [Wikipedia Artikel](https://en.wikipedia.org/wiki/Maze_generation_algorithm) und [Sammlung von Jamis Buck](https://weblog.jamisbuck.org/2011/2/7/maze-generation-algorithm-recap) geben einen sehr guten Einblick in die Thematik.

#### Zimmerplatzierung

Die Platzierung der Zimmer erfolgt über die Klasse **Quadtree**.  Ein [Quaternärbaum](https://de.wikipedia.org/wiki/Quadtree) (Engl. Quadtree) ist ein besonderer Baum mit genau vier Kinderknoten.  Mit dessen Hilfe lässt es sich sehr effizient eine nicht überlapbare Platzierung für die Zimmer zu finden.
Der Baum nimmt die ganze Fläche entgegen und teilt dies in viel kleinere Vierecken, wobei jeder Viereck  wird am Ende ein Zimmer beinhalten.

Die Klasse **BSPDungeon** bringt dann alles zusammen, generiert das Labyrinth mit dem gegebenen Verfahren, platziert die Zimmer zufällig und stellt den oberen Hierarchien Methoden zum Verwalten.


### die Entitäten

Alle Objekte in der Spielwelt sind Entitäten.  Entitäten besitzen im Kern eine Hashtabelle über alle verfügbaren Fähigkeiten, die ein- bzw. ausgeschaltet werden können, ein Inventar, die Stelle in der Welt und einen Zustandskomponent, der dann bestimmt, ob eine Entität noch lebendig ist oder nicht und inkrementiert dessen Energie.


#### die Entitätskomponente

Die Verhaltensweise der Entitäten werden mithilfe von Entitätskomponente - **DungeonComponent** - bestimmt.  Diese Klasse hat eine wichtige Methode **perform()**, die alle Unterklassen zum Überschreiben verpflichtet sind.
Außerdem gibt es nocht die methode getCost(), die besagt, wieviel Energie eine Entität braucht, um diese Aktivität durchführen.



### Inventar- bzw. Objektsystem

Das Inventarsystem ist an sich ein interessantes Konzept.  Es können gleichzeitig eine begrenzte Anzahl von Objekten getragen werden.  Zusätzlich gilt die Bedingung, dass die Entität immer auf den neuest hinzugefügten Gegenstand zugreifen kann. Z.B. wenn die Hauptfigur zwei Messer besitzt, eine mit sehr schwachem Stand und der andere neu und scharf, kann sie die zwei so anordnen, dass sie immer die bessere Messer herauszieht.


Es gibt auch Gegenstände.  Nun ein Gegenstand ist wieder an sich eine Entität.  Diese Vermischung von Entität und Gegenstand erlaubt komplexere Systeme zu bauen, etwa wie Gegenstände, die wiederum ein Inventar haben dürfen.


### KI

Die Künstliche Intelligenz stützt sich auf das Konzept namens "Utility System".  Für jede Entität wird eine Liste von Prioritäten erzeugt, die dann benutzt wird, um die meist notwendigste Begehrung zu erfüllen.

#### Idee des Bewertungssystem

Als Beispiel nehmen wir an, dass in einem Raum sich einen Gegenstand und den Helden befinden.  Man fragt das die KI, eine passende Entscheidung zu fällen.  Da der Gegenstand für die Entität mehr anreizend ist als der Spieler, so wird es versucht, sich den Gegenstand zu holen.  Scheitert die KI, die Begehrung zu erfüllen, etwa weil der Gegenstand nicht mehr in der Welt ist - es wurde 
beispielsweise von der Hauptfigur genommen - setzt sie mit dem Entscheidungsprozess weiter fort und fängt an, die Hauptfigur zu verfolgen, da sie zur Zeit keinen höheren Anreiz hat.

Der Code dafür ist in der Datei **DungeonAIFactory** zu finden, was zu Methoden aus **DungeonAIUtil** greift, um eine Liste von KIs zu generieren.  Erst danach wird die KI mit den meisten Punkten ausgewählt.

#### umgesetzte Beispiele

Es sind drei verschiedene Verhaltensweisen implementiert.  Eine KI, die sich immer den nähersten Gegenstand holt, eine KI, die immer die näherste Entität verfolgt, und eine KI, die sich einen festen Punkt auf der Karte annähert.
Der Raum für die Bewertung liegt offen.  Man kann auch verschiedene Eigenschaften des Ziels in Betracht ziehen.  [Dieser Artikel](https://www.roguebasin.com/index.php/Need_driven_AI) auf Roguebasin zählt ein paar Möglichkeiten auf.


### Zeitschritt in der Welt

Jede lebendige Entität (kein Gegenstand) verfügt über eine Art von Energiepunkten.  Wenn sie nun zum Verhalten auffordert wird, schaut das System erst, ob sie ausreichende Energie dafür hat.  Das geschieht dadurch, dass jedes Verhalten eine bestimmte Anzahl von Energiepunkten braucht, oder 0, für die einmaligen Verhalten.  Die Methode **getCost()** in der **DungeonComponent** Klasse wird dementsprechend überschrieben.
Besitzt die Entität genug Energiepunkten, wird das nächste Verhalten von dem System ausgeführt - **getNextAction()** die dann eine Unterklasse der **DongeonComponent** ist.

Ein vollständiger Durchlauf in der Welt lässt sich folgendermaßen vorstellen:

* Die Hauptfigur nutzt alle Energiepunkte und führt Aktionen aus
* Dann wird es über alle anderen Entitäten durchiteriert, und dabei werden dessen Energiepunkte erhöhrt - **perform()** auf **DungeonComponentStat** aufgerufen - wenn sie keine vorgeplante Aktivität haben, oder die Aktivität ausgeführt und wieder Energiepunkte abgezogen.
* Wieder zu der*dem Held*in ...


### Held*in

Der Held bzw. die Heldin ist nun eine normale Entität.  Zum einlesen von dessen Befehlen ist aber einen Textanalyser eingebaut, der auf die Angabe des/der Benutzer*in eingeht, und entweder löst eine Tätigkeit aus oder liefert Information von der Welt.
Etwa wie die Anweisung "look/l/L/Look", die herumliegende Umgebung mithilfe eines Shadow-Casting-Algorithmus ermittelt.
Der dafür zuständige Code ist in **DungeonEntityHeroInputHandler** zu finden.

# Szenarien

Nun widmen wir  uns dem Thema "Szenarien" zu.  Es sind fünf generelle Szenarien  bei dem Funktionsversprechen vorgesehen.

## Szenario 1

> Ein flexibles System, bei dem unterschiedliche Lebewesen leicht hinzuzufügen bzw. zu 
> verändern sind. Hier geht es mehr darum, einen Mechanismus einzubauen, der das Anlegen eines 
> Lebewesens erleichtert. Zum Beispiel die Möglichkeit, dass einen neuen Dämon mithilfe 
> Json zu definieren, der über mehrere Attribute verfügt.
> Es kann z.B. im Jshell auf die Befehle zum Einlesen und Exportieren von Kreaturen zugegriffen
> werden.
> Der Sinn des Exportfeatures liegt darin, dass es die Möglichkeit geben kann, dass der 
> Generieralgorithmus neue Lebewesen anlegt, indem er andere Typen miteinander 
> kombiniert, oder der Spieler wäre in der Lage, falls einmal das Spiel mit Erfolg beendet zu 
> haben, neue Art von Kreaturen erstellen.
> Die Anweisungen zum Einlesen/Exportieren bzw. Anlegen eines Lebewesens sehen 
> folgendermaßen aus:
> Metro.loadCreatures(String filename);
> Metro.exportCreatures(String filename);
> Metro.newCreature(String parameters); // Als Json-Format angegeben

Hier gibt es zwar die Möglichkeit, Kreaturen bzw. Gegenstände mit verschiedenen vordefinierten Startzuständen anzulegen, ist jedoch nicht die Funktionalität eingebaut, dass man leicht neue Rassen bzw.  Kreaturklassen einführt.
Der Grund liegt daran, dass sich der Autor der Ansicht ist, dass es keinen Sinn ergibt, neue Art von Kreaturen zu anlegen, wenn es nicht die Möglichkeit dafür vorgesehen ist, dass man  die Verhalten aus externen Dateien  einlädt.  Skriptsprachen wie [LUA](https://de.wikipedia.org/wiki/Lua) sind dafür geeignet, aber aus Zeitgründen war es nicht gelungen, so ein dynamisches System umzusetzen.

Die drei erwähnten Methoden gelten hier.  Das zeigen wir anhand eines Beispiel.  
Wir lesen  eine Datei namens "test.json" ein, listen die möglichen Kreaturen bzw. Gegenständen auf, die sich erstellen lassen, führen die Eigenschaften einer anderen Rasse ein, und speichern dann alles in der Datei namens "testEdited.json" ab.
"""
 );


        Clerk.markdown(Text.fillOut("""
``` Java
        ${0}

        ${1}
        ${2}
        ${3}
```
""",
        Text.cutOut("../src/Codedoku.java", "// scenario1_1"),         scenario1_1(), Text.cutOut("../src/Codedoku.java", "// scenario1_2"),         scenario1_2()
    ));
    
        Clerk.markdown(Text.fillOut("""
## Szenario 2

> Beide – die Hauptfigure und KI-gesteuerte Lebewesen – verfügen über eine Art Inventars. 
> Es können Objekte darin abgestellt werden bzw. davon entfernt werden. Das Inventar 
> vergrößert sich je nach Level oder die Rassenklasse des Tragenden.

        Schon  hingewiesen, sind alle definierten Kreaturrassen in der Lage, ein Inventar zu haben.  Wir greifen das Beispiel von eben auf, legen ein neues Objekt der Rasse  **rat** an und geben ihm Käse zum Abendessen :)

``` Java
        ${0}
        ${1}
```
        
        Wie es ersichtlich ist, kann die Ratte bis maximal fünf Gegenständen tragen. Das Interessante daran ist, dass alle Gegenstände definierte Gewichte haben.  Überschreitet man die Obergrenze der tragbaren Gegenstände bei einem Inventar, wird es dann eine Fehlermeldung ausgelöst.

``` Java
        ${2}
        ${3}
```
        """, Text.cutOut("../src/Codedoku.java", "// scenario2_1"),         scenario2_1(), Text.cutOut("../src/Codedoku.java", "// scenario2_2"),         scenario2_2()
        ));

    Clerk.markdown(Text.fillOut("""
## Szenario 3

> Verschiedene Klassen sind im Spiel implementiert, etwa wie Mensch oder Schwarz (eine Art 
> Mutanten). Der Benutzer hat die Möglichkeit, am Anfang seine/ihre Rasse auszuwählen.
> Diese ermöglicht ihr/ihn später dann, einfacher durch schon erworbene Eigenschaften den 
> Situationen zu entkommen.

Wir setzen das Beispiel von der Ratte fort.  Nun aber wollen wir eine kampffähige Ratte  hinzufügen, die dann drei Fähigkeiten hat.

* **movement**
    Die Ratte kann sich bewegen.
* **melayattack**
    Die Ratte kann mit Gegenständen andere Entitäten angreifen.
* **drop**
    Die Ratte kann Gegenstände aus der Hand fallen lassen.

``` Java
${0}
${1}
```
""", 
Text.cutOut("../src/Codedoku.java", "// scenario3"), 
scenario3()
));

        Clerk.markdown(Text.fillOut("""
## Szenario 4

> Labyrinthe lassen sich effizient mittels verschiedener Algorithmen wie Kruskal generieren. 
> Hier geht es auch darum, dass der Computer sowohl die Spielwelt als auch die Kreaturen 
> und Objekte automatisch erzeugt und es werden eine minimale Anzahl an menschlicher 
> Interaktivität erforderlich.

Die prozedurale Generierung ist ein wichtiger Aspekt bei einem Rogue-Like.
Aus diesem Grund ermöglicht die Schnittstelle dem/der Designer*in, neue Algorithmen für die Labyrinthentstehung zu implementieren.
Die Klasse **MazeGenerator** bietet die abstrakte Methode 
``` java
    void generate(Grid grid, DungeonPoint startMaze, DungeonPoint endMaze);
```
an, auf den sich verschiedene Algorithmen aufbauen lassen.  Die Methode wird dann direkt bei der Weltanschaffung - constructor World - aufgerufen.

Wir zeigen nun anhand eines kleinen Beispiels, wie die drei generierten Labyrinthe aussehen

### der Originalgrid

Das ist ein 10-mal-10 Grid, der derzeit nicht nicht gebaut worden ist.

``` java
${0}
${1}
```


### Sidewinder

``` java
${2}
${3}
```

### der Backtracker

``` java
${4}
```

### der Aldous-Broder bzw. Drunkards Walk

``` java
${5}

```
""", Text.cutOut("../src/Codedoku.java", "// scenario4_1"), scenario4_1(), Text.cutOut("../src/Codedoku.java", "// scenario4_2"), scenario4_2_grid("sidewinder"), scenario4_2_grid("backtracker"), scenario4_2_grid("aldousbroder")));

        Clerk.markdown(Text.fillOut("""
                        
## Szenario 5

> Der Spieler soll in der Lage sein, später während des Ablaufs sein*ihren Fortschritt zu
> speichern. Das erfolgt nur dann, wenn der Spieler / die Spielerin nicht später getötet
> wird. In solchem Fall ist das Spiel im Friedhofzustand (Engl. Graveyard) und kann
> nicht weiter geladen werden.
                        
                        Dieses Verfahren ist mittels direktes Schreiben der Objekte im Binärformat [Serialisierung](https://de.wikipedia.org/wiki/Serialisierung) umgesetzt.  Als ein Prototyp eignet sich ja die Lösung, aber es ist keine gute Lösung, und dafür sind hier drei wichtige Aspekte, die man in Erwägung ziehen sollte:
* Die gespeicherten Daten sind nicht rückwärtskompatibel.  Eine kleine Änderung in den Klassen bricht die Struktur zusammen und macht das ganze Spiel unlädbar.
*  Manche Objektvariablen - wie der EntityDataManager bei der World Class - braucht  als "Transient" markiert zu werden.  Das ist an sich schadenlos, allerdings kann ja auch problematisch werden, wenn z.B. das Spiel neu geladen wird.  Dann werden die jeweiligen Objektvariablen auf null gesetzt und es müssen Überprüfungen stattfinden, um zu versichern, dass keine unerwarteten Fehler auftauchen

Hier weicht auch die gelieferte Implementierung von dem Funktionsversprechen ab, indem es keine Mechanismen zur Löschung von Spielständen vorgesehen worden sind.  Der Autor ist der Meinung, dass sich dieses Problem schwer lösen lässt.  Denn ist ja "Permadeath" zwar einen wichtigen Stützpunkt bei Rogue-Likes, und die Idee, dass man, wenn schon früher bei einem geladenen Spiel getötet worden ist, nicht wieder denselben Spielstand laden darf, aber sind die dafür Lösungen leicht umgehbar, etwa wie beim Kopieren der Spielstanddatei.

Die zwei dazu vorgesehenen Methoden 

``` java
    void save(String filename);
    public static World load(String filename);
```
dienen diesem Zweck gut.  
Wir zeigen anhand eines Beispiels.  Wir krearieren eine Welt mit einer einzigen Ratte, die sich das Käse holt und speichern ab.  Bei dem direkten Laden wird es ersichtlich, dass die Ratte noch das Käse im Inventar hat und es befinden weiterhin kein Käseobjekt in der Welt.
```
${0}
${1}

```
""", Text.cutOut("../src/Codedoku.java", "// scenario5_1"),          Text.cutOut("../src/Codedoku.java", "// scenario5_2")
));
        Clerk.markdown("## Vor der Speicherung ");
        scenario5_1();
        Clerk.markdown("## nach der Ladung");

        scenario5_2();
        Clerk.markdown(Text.fillOut("""
# Ausblick: Eine Welt mit einer kleinen Rattenfamilie

Nun ist es soweit liebe*r Leser*in, Sie haben sich den Stoff durchgelesen und jetzt sind - hoffentlich interessiert, dies alle in Praxis vor Augen geführt zu bekommen.  In diesem Abschnitt werde ich genau das Tun!
Das kleine Beispiel liegt eine kleine 15-mal-15 Welt zugrunde, mit 5 Ratten und beliebig Käsen.  Es werden die ersten 20 Schritte der Welt veranschaulicht, und jenach 4 Schritten die Welt wieder mal angezeigt.  Zum Glück nehmen alle Familienmitglieder genug Käse  zu Hause mit, und der Autor verabschiedet sich an dieser Stelle von Ihnen. Vielen Dank für die Zeit!

        """
        ));
        Clerk.markdown("## Simulation");
        ausblick();
    }

    // scenario1_1
    public static String scenario1_1() {
        DungeonEntityDataManager d=DungeonEntityDataManager.loadFile("test.json");
        return printDataManager(d);
    }
    private static String printDataManager(DungeonEntityDataManager manager) {
        StringBuilder builder = new StringBuilder();        
        manager.listEntities()
        .forEach(e ->
            builder.append(e));
            return builder.toString();
    }
    // scenario1_1
    // scenario1_2
    public static String scenario1_2() {
        DungeonEntityDataManager manager=DungeonEntityDataManager.loadFile("test.json");
        manager.newEntity("""
    {
        "race": "RAT",
        names: "ratNames",
        "classes": [
            {
	            "type": "GENERIC",
                "abilities": ["ITEMPICKUP", "MOVEMENT", "INTERRACTION"],
                "inventory": {
	                "totalWeightAllowed": 5,
                    "items": [
					]
				},
				"stats": {
					"energy": 0.5,
					"recoverEnergy": 0.9,
					"vision": 3,
					"maxVision": 3,
					"hitpoints": 3,
					"maxHitpoints": 3,
					"str": 4,
					"maxStr": 5,
					"dex": 3,
					"maxDex": 4,
					"con": 6,
					"maxCon": 6,
					"wis": 10,
					"maxWis": 10,
					"intel": 7,
					"maxIntel": 7,
					"mana": 10,
					"maxMana": 10,
					"thaco": 20,
					"armourClass": 5,
					"level": 2
				}
			} ]
    }
                """
    );
    manager.newEntity("""
	{
		"race": "ITEM",		
		"categories": [
			{
				"name": "chese",

				"stats": {
					"type": "FOOD",
					"energy": 1.0,
					"recoverEnergy": 1.0,
					"hitpoints": 5,
					"maxHitpoints": 5,
					"str": 0,
					"maxStr": 0,
					"dex": 0,
					"maxDex": 0,
					"con": 0,
					"maxCon": 0,
					"wis": 0,
					"maxWis": 0,
					"intel": 0,
					"maxIntel": 0,
					"mana": 0,
					"maxMana": 0,
					"thaco": 0,
					"armourClass": 10,
					"level": 1,
					"isPickable": true,
					"weight": 1,
					"throwDP": {
						"dSides": 1,
						"dNumbers": 0,
						"negativeDamage": false,
						"dist": 0
					},
					"melayDP": {
						"dSides": 0,
						"dNumbers": 0,
						"negativeDamage": false,
						"dist": 0
					},
					"rangeDP": {
						"dSides": 0,
						"dNumbers": 0,
						"dist": 0,
						"negativeDamage": false
					}
				}
			}
		]
	}
    
"""
    );
    try {
        manager.exportEntities("testEdited.json");
    }
    catch(Exception e) {

    }
    return printDataManager(manager);
    }
    // scenario1_2
    // scenario2_1
    private static String  scenario2_1() {
        String result="";
        DungeonEntityDataManager d=DungeonEntityDataManager.loadFile("testEdited.json");
        DungeonEntity rat=d.createCreature(null, null, DungeonEntityRace.RAT, DungeonCreatureClass.GENERIC);
        result+=rat.toString();
        result+="\n";
        DungeonEntity item1=d.createItem("chese", null);
        DungeonEntity item2=d.createItem("chese", null);
        try {
        rat.getInventory().add(item1);
        rat.getInventory().add(item2);

        }
        catch(Exception e) {

        }
        result+=rat.toString();
        return result;
    }
    // scenario2_1
    // scenario2_2
    private static String scenario2_2() {
        String result="";
        DungeonEntityDataManager d=DungeonEntityDataManager.loadFile("testEdited.json");
        DungeonEntity rat=d.createCreature(null, null, DungeonEntityRace.RAT, DungeonCreatureClass.GENERIC);
        result+=rat.toString();
        result+="\n";
        DungeonEntity item1=d.createItem("knife", null);
        DungeonEntity item2=d.createItem("knife", null);
        try {
        rat.getInventory().add(item1);
        rat.getInventory().add(item2);

        }
        catch(Exception e) {
            result+=e.getMessage() + " reason: "+ e.getCause();
        }
        result+=rat.toString();
        return result;

    }
    // scenario2_2

    // scenario3
    private static String scenario3() {
        DungeonEntityDataManager manager=DungeonEntityDataManager.loadFile("testEdited.json");
        manager.newEntity("""
    {
        "race": "RAT",
        "classes": [
            {
	            "type": "WARRIOR",
                "abilities": ["MOVEMENT", "MELAYATTACK", "ITEMPICKUP"],
                "inventory": {
	                "totalWeightAllowed": 10,
                    "items": [
					]
				},
				"stats": {
					"energy": 1.0,
					"recoverEnergy": 0.4,
					"vision": 5,
					"maxVision": 5,
					"hitpoints": 6,
					"maxHitpoints": 6,
					"str": 6,
					"maxStr": 6,
					"dex": 6,
					"maxDex": 6,
					"con": 6,
					"maxCon": 6,
					"wis": 10,
					"maxWis": 10,
					"intel": 7,
					"maxIntel": 7,
					"mana": 10,
					"maxMana": 10,
					"thaco": 20,
					"armourClass": 3,
					"level": 2
				}
			} ]
    }
"""
        );
        var warrior = manager.createCreature("", null, DungeonEntityRace.RAT, DungeonCreatureClass.WARRIOR);
        try {
            manager.exportEntities("testEdited2.json");
        }
        catch (Exception e) {
        }
        return warrior.toString();
    }
    // scenario3
    // scenario4_1
    private static  String scenario4_1() {
        return new Grid(10, 10).toString();
    }
    // scenario4_1
    // scenario4_2_grid
    private static  String scenario4_2_grid(String whatalgo) {
        Grid g= new Grid(10, 10);
        MazeGenerator mg=null;
        switch(whatalgo.toLowerCase()) {
            default:
                break;
            case "sidewinder":
                mg = new Sidewinder();
                break;
            case "backtracker":
                mg = new Backtracker();
                break;
            case "aldousbroder":          
                mg = new AldousBroder();
                break;
        }
        if(mg == null) {
            return "";
        }
        mg.generate(g, new DungeonPoint(0, 0), new DungeonPoint(9, 9));
        return g.toString();
    }
    // scenario4_2_grid
    // scenario5_1
    private static void scenario5_1() {
        Logger rootLogger = LogManager.getLogManager().getLogger("");
         rootLogger.removeHandler(rootLogger.getHandlers()[0]);
        World w=new World(10, 10, new AldousBroder());
        Room r=w.dungeon.getRooms().get(DungeonUtil.random.nextInt(0, w.dungeon.getRooms().size()));
        try {
            w.loadJSONEntities("testEdited2.json");
            w.spawnCreature(r.getCenter(), null, DungeonEntityRace.RAT, DungeonCreatureClass.WARRIOR, true);
            w.spawnItem(new DungeonPoint(r.getCenter().getX()+1, r.getCenter().getY()), "chese");        
        }
        catch(Exception e) {

        }
        DungeonEntityHeroInputHandler.setWorld(w);
        DungeonEntityHeroInputHandler.setEntityHandler(new DungeonClerkOutputHandler());
        DungeonEntityHeroInputHandler.process("show stats");
        DungeonEntityHeroInputHandler.process("look");
        DungeonEntityHeroInputHandler.process("go south");
        DungeonEntityHeroInputHandler.process("look");
        
        DungeonEntityHeroInputHandler.process("pickup");
        DungeonEntityHeroInputHandler.process("show stats");
        DungeonEntityHeroInputHandler.process("look");
        DungeonEntityHeroInputHandler.process("save");
        
    }
    // scenario5_1
    // scenario5_2
    private static void scenario5_2() {
        Logger rootLogger = LogManager.getLogManager().getLogger("");
        // rootLogger.removeHandler(rootLogger.getHandlers()[0]);
        World w=null;
        
        try {
            w=World.load("gameSave");

            w.loadJSONEntities("testEdited2.json");
        }
        catch(Exception e) {
            System.err.println(e.getMessage());
        }
        DungeonEntityHeroInputHandler.setWorld(w);
        DungeonEntityHeroInputHandler.setEntityHandler(new DungeonClerkOutputHandler());
        DungeonEntityHeroInputHandler.process("show stats");
        DungeonEntityHeroInputHandler.process("look");
    }
    // scenario5_2
    private static void  ausblick() {
        World w=new World(15, 15, new AldousBroder());
        try {
            w.loadJSONEntities("testEdited.json");
            
            Room r=w.dungeon.getRooms().get(DungeonUtil.random.nextInt(0, w.dungeon.getRooms().size()));
            
            DungeonPoint point = new DungeonPoint(DungeonUtil.random.nextInt(r.getTop().getX(), r.getBottom().getX()), DungeonUtil.random.nextInt(r.getTop().getY(), r.getBottom().getY()));
            w.spawnCreature(point, null, DungeonEntityRace.RAT, DungeonCreatureClass.GENERIC, true);
        
            
            generateRandom(w);
        
        }   
        catch(Exception e) {
System.out.println(e.getMessage());
        }   
        DungeonEntityHeroInputHandler.setWorld(w);
        DungeonEntityHeroInputHandler.setEntityHandler(new DungeonClerkOutputHandler());
        DungeonEntityHeroInputHandler.process("show stats");
        DungeonEntityHeroInputHandler.process("look");
        DungeonEntity h=w.getHero();
        for(int i=0; i<20; i++) {
            h.setNextAction(DungeonComponent.DUMMY);
            if(i%4==0) {
                Clerk.markdown("### Zeitschritt "+(i+1));
                DungeonEntityHeroInputHandler.process("look");
            }
        }
    }
    private static void generateRandom(World w)  throws Exception {
            Room r = null;
            DungeonPoint point = null;
        for(int i=0; i<4; i++) {
            try {
                r=w.dungeon.getRooms().get(DungeonUtil.random.nextInt(0, w.dungeon.getRooms().size()));
                point = new DungeonPoint(DungeonUtil.random.nextInt(r.getTop().getX(), r.getBottom().getX()), DungeonUtil.random.nextInt(r.getTop().getY(), r.getBottom().getY()));
                var e=w.spawnCreature(point, null, DungeonEntityRace.RAT, DungeonCreatureClass.GENERIC, false);
                e.setAI(true);
            }
            catch(Exception e) {
                continue;
            }
        }
            for(int i=0; i<20; i++) {
                try {
                    r=w.dungeon.getRooms().get(DungeonUtil.random.nextInt(0, w.dungeon.getRooms().size()));
                    point = new DungeonPoint(DungeonUtil.random.nextInt(r.getTop().getX(), r.getBottom().getX()), DungeonUtil.random.nextInt(r.getTop().getY(), r.getBottom().getY()));
                    w.spawnItem(point, "chese");
                }
                catch(Exception e) {
                }
            }
        }
    
}
