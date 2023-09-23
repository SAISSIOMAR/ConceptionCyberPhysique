# Conception Cyber-Physique

## Étudiants
- Marouane Boubia
- Omar Saissi
- Ayman ElFiguigui
- Youssef ElGarmit

## Numéro du groupe : [Insérez le numéro du groupe ici]

## Titre temporaire du service ambiant
SoundGuard : Système Intelligent de Gestion du Bruit

## Environnements de mise en œuvre
- Salle de classe
- Bureau

## Domaine
Système de gestion environnementale, Internet des Objets (IoT), Cyber-Physique

## Description
SoundGuard est une solution de gestion du bruit novatrice, exploitant des LEDs pour une communication intuitive de l'environnement sonore. Les LEDs signalent le dépassement des normes en s'allumant en rouge, clignotent en cas d'urgence, ou changent de couleur selon un code défini pour indiquer le niveau sonore. Des notifications sonores discrètes peuvent également accompagner ces indicateurs lumineux pour alerter les occupants, simplifiant la compréhension et le respect des normes sonores dans chaque salle.

## Motivations
Le projet SoundGuard s'avère être une réponse indispensable à plusieurs enjeux cruciaux :
1. **Conformité Réglementaire :** SoundGuard est conçu en stricte conformité avec la réglementation française en matière de bien-être au travail et de qualité de l'environnement. Il garantit que le niveau sonore demeure en permanence en dessous du seuil réglementaire de 85 décibels. Ainsi, il permet aux écoles et aux entreprises de se conformer pleinement aux lois et réglementations en vigueur, évitant ainsi les sanctions potentielles.
   - Le Code du Travail français, notamment les articles R4222-1 à R4222-21 concernant les niveaux sonores au travail.
   - Les réglementations en matière de bruit au travail établies par l'Agence nationale de sécurité sanitaire de l'alimentation, de l'environnement et du travail (ANSES).
2. **Amélioration du Bien-être :** Dans un monde où le bruit excessif est devenu une source majeure de stress et de distraction, SoundGuard répond à une demande croissante d'espaces de travail et de salles de classe offrant un environnement calme et propice à la concentration, à l'apprentissage et à la productivité. En créant des environnements plus silencieux, le projet vise à améliorer significativement le bien-être et la qualité de vie des utilisateurs.
   - "Noise Pollution and Health Effects" de l'Organisation mondiale de la santé (OMS).
   - Les travaux de la Direction Générale de la Santé (DGS) en France concernant la santé environnementale.
3. **Optimisation des Performances :** Le bruit excessif dans les environnements éducatifs et professionnels a été associé à une diminution des performances cognitives, de la productivité et de la créativité. SoundGuard aide à optimiser les performances des élèves et des employés en créant des conditions idéales pour l'apprentissage et le travail, favorisant ainsi une meilleure concentration et des résultats plus élevés.
   - "The Effects of Noise on Cognitive Performance" publié dans le journal Noise & Health ([Lien](https://www.ncbi.nlm.nih.gov/pmc/articles/PMC6901841/))

## Analyse de l'existant
Avant de concevoir SoundGuard, une recherche bibliographique approfondie a été effectuée pour recueillir des informations sur les systèmes existants de gestion du bruit, y compris ceux utilisant des capteurs IoT et des systèmes de surveillance acoustique. Il a été constaté que de tels systèmes existent, mais leur automatisation et leur intégration complètes sont limitées. SoundGuard vise à combler cette lacune en fournissant une solution intelligente et complète de gestion du bruit.

**Lien Existant :** [NoiseAware](https://noiseaware.com/)
- Description : NoiseAware est une entreprise qui propose un système de gestion du bruit basé sur IoT pour les propriétaires de locations de vacances et les gestionnaires de biens immobiliers. Bien que ce système soit principalement conçu pour les locations de vacances, il illustre l'utilisation de l'IoT pour la surveillance du bruit dans un environnement de travail (dans ce cas, un environnement de travail locatif).
- Capteurs de bruit IoT : NoiseAware fournit des capteurs de bruit qui sont installés dans les propriétés locatives. Ces capteurs surveillent en permanence les niveaux de bruit.
- Alertes en temps réel : Lorsque les niveaux de bruit dépassent un seuil prédéfini, le système envoie des alertes en temps réel aux propriétaires ou aux gestionnaires de biens, leur permettant de prendre des mesures immédiates.

## Schéma Architectural de Principe
SoundGuard repose sur une architecture à trois niveaux :
1. **Capteurs et Dispositifs :** Des capteurs de bruit IoT sont déployés dans les salles de classe et les bureaux. Ces capteurs surveillent en continu les niveaux de bruit environnants.
2. **Unité de Contrôle :** Une unité centrale de traitement reçoit les données des capteurs IoT en temps réel comme une Raspberry. Cette unité analyse les données, compare les niveaux sonores aux seuils réglementaires et prend des décisions en fonction de ces informations.
3. **Dispositifs de Contrôle :** Lorsque les niveaux de bruit dépassent les seuils réglementaires, le système active des dispositifs de contrôle dans notre cas les LEDs.

## Éléments techniques pour lever les premiers verrous techniques

### Matériel nécessaire :
- **NeoPixel LEDs :**
   - Description : Les NeoPixels (ou WS2812B) sont des LEDs RVB programmables individuellement qui peuvent être chaînées ensemble pour créer des effets lumineux.
   - API : Pour contrôler les NeoPixels, la bibliothèque "Adafruit NeoPixel" est couramment utilisée.
   - Exemple de code : [Adafruit NeoPixel Uberguide](https://learn.adafruit.com/adafruit-neopixel-uberguide/arduino-library-use)

- **Arduino avec un Capteur de Son KY-038 :**
   - Description : Le capteur de son KY-038 est un module de microphone pour Arduino qui peut être utilisé pour détecter des niveaux sonores.
   - API : Arduino propose des bibliothèques pour lire les données du capteur de son KY-038. [Bibliothèque Arduino pour KY-038](https://sensorkit.joy-it.net/fr/sensors/ky-038)

- **Grove - Sound Sensor :**
   - Description : Le capteur de son Grove est un module de microphone qui peut également être utilisé avec un Raspberry Pi pour détecter les niveaux de son dans l'environnement.
   - API : Pour utiliser le capteur de son Grove avec un Raspberry Pi, nous pouvons le connecter à un Grove Base Hat ou à un GrovePi et utiliser des bibliothèques Python pour lire les données du capteur.

### Capteur de bruit :
- Module microphone Electret : Un module de capteur microphonique de base adapté à la détection des sons.

### Unité de traitement :
- Raspberry Pi (par exemple, Raspberry Pi 4) : Un mini-ordinateur capable de traiter les données des capteurs, d'exécuter un serveur et de gérer des tâches plus complexes.

### Bande LED RVB

### Connectivité :
- Carte SD : comme nous utilisons un Raspberry Pi, nous aurons besoin d'une carte SD pour stocker le système d'exploitation et notre code.

### Interface Web/API pour l'Affichage des Données :
- Créez une interface web sur le Raspberry Pi pour afficher les niveaux sonores en temps réel et les alertes.
- Les utilisateurs pourront accéder à cette interface depuis un navigateur pour surveiller le niveau sonore de la salle.

## Exemple d’utilisation de notre projet

**Matériel nécessaire :**
- Raspberry Pi (n'importe quel modèle compatible avec GPIO)
- Capteur de bruit
- LEDs (deux LEDs connectées aux broches GPIO de la Raspberry Pi)

**Bibliothèques :**
- RPi.GPIO : Pour contrôler les broches GPIO de la Raspberry Pi.
- python-mqtt : Pour la communication MQTT avec les capteurs (facultatif, en fonction de votre configuration).

**Pseudo code (python) :**

Ce code surveille en continu les niveaux sonores à partir du capteur de bruit via MQTT, active les LEDs si le niveau sonore dépasse le seuil, et les désactive sinon.

```import RPi.GPIO as GPIO
import time
import paho.mqtt.client as mqtt

# Configuration des broches pour les LEDs
GPIO.setmode(GPIO.BCM)
LED_PIN1 = 17  # Broche GPIO pour la première LED
LED_PIN2 = 18  # Broche GPIO pour la deuxième LED
GPIO.setup(LED_PIN1, GPIO.OUT)
GPIO.setup(LED_PIN2, GPIO.OUT)

# Configuration du client MQTT pour recevoir les données des capteurs
client = mqtt.Client()
client.connect("broker.mqtt.com", 1883)
client.subscribe("topic/capteurs")

def on_message(client, userdata, message):
    niveau_sonore = float(message.payload)
    
    if niveau_sonore > seuil_reglementaire:
        activer_LEDs()
    else:
        desactiver_LEDs()

def activer_LEDs():
    GPIO.output(LED_PIN1, GPIO.HIGH)
    GPIO.output(LED_PIN2, GPIO.HIGH)

def desactiver_LEDs():
    GPIO.output(LED_PIN1, GPIO.LOW)
    GPIO.output(LED_PIN2, GPIO.LOW)

# Configuration du callback MQTT
client.on_message = on_message

# Boucle principale
try:
    while True:
        client.loop_start()  # Démarre le client MQTT
        time.sleep(1)  # Attend une seconde entre les lectures
except KeyboardInterrupt:
    client.disconnect()  # Déconnexion propre du client MQTT lors de l'arrêt du programme
    GPIO.cleanup()  # Nettoyage des broches GPIO lors de l'arrêt du programme
```



## Annexe

**Liens Utiles :**

- [Loi sur le bruit au travail](https://travail-emploi.gouv.fr/sante-au-travail/prevention-des-risques-pour-la-sante-au-travail/autres-dangers-et-risques/article/bruit-en-milieu-de-travail)
  - Description : Lien vers le site officiel du Ministère du Travail français, où vous pouvez trouver des informations sur la réglementation en matière de bruit au travail.
 

- [Limitation du Bruit dans les Établissements d'Enseignement](https://www.legifrance.gouv.fr/loda/id/LEGITEXT000005634431)
  - Description : Lien vers la législation française concernant la limitation du bruit dans les établissements d'enseignement. Cette législation peut être utile pour comprendre les normes et les réglementations en vigueur pour les environnements éducatifs.


- [Capteur de décibel](https://fr.aliexpress.com/item/1005002390552167.html?src=google&src=google&albch=shopping&acnt=248-630-5778&slnk=&plac=&mtctp=&albbt=Google_7_shopping&gclsrc=aw.ds&albagn=888888&isSmbAutoCall=false&needSmbHouyi=false&src=google&albch=shopping&acnt=248-630-5778&slnk=&plac=&mtctp=&albbt=Google_7_shopping&gclsrc=aw.ds&albagn=888888&ds_e_adid=&ds_e_matchtype=&ds_e_device=c&ds_e_network=x&ds_e_product_group_id=&ds_e_product_id=fr1005002390552167&ds_e_product_merchant_id=107869342&ds_e_product_country=FR&ds_e_product_language=fr&ds_e_product_channel=online&ds_e_product_store_id=&ds_url_v=2&albcp=20180143332&albag=&isSmbAutoCall=false&needSmbHouyi=false&gclid=CjwKCAjwmbqoBhAgEiwACIjzEAdJqVVfmVnuVpzGOZZXPORav_Md49N7f30Ay1b5U72Ub49fL9PHfxoCRlgQAvD_BwE&aff_fcid=c2d6fd7753ce4401b9831a2fc909b565-1695487910096-08064-UneMJZVf&aff_fsk=UneMJZVf&aff_platform=aaf&sk=UneMJZVf&aff_trace_key=c2d6fd7753ce4401b9831a2fc909b565-1695487910096-08064-UneMJZVf&terminal_id=a9c2e50af74242debcab62a32ce9cbe4&afSmartRedirect=y)
  - Description : Lien vers une boutique en ligne où l'on peut trouver des capteurs de décibels pour ce projet.

