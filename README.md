# Wer läuft hat Recht

Backend der responsiven Web-App für den Schrittzähler-Wettbewerb der Justiz Brandenburg. In der Anwendung können die Nutzer regelmäßig innerhalb eines Wettbewerbs ihre gelaufenen Schritte eintragen und so untereinander innerhalb ihrer Institution und miteinander gegen die anderen Institutionen konkurrieren.

## Features:

### Für Gesundheitsmanager/in (am Desktop):
- Verwaltung von:
  - Schrittzählwettbewerben
  - Allen Gerichten
  - Nutzern aller Gerichte
- Einsehen der Ergebnisse (gruppiert nach allen Teilnehmern eines Gerichts und Gerichtsrangliste)
- Export der Ranglisten

### Für andere Nutzer:
- Einfache und flexible Eingabe von Schrittzahlen (Zeitraum festlegen, Standard: heutiger Tag)
- Bearbeitung von Schrittzahlen für bereits erfasste Tage
- Übersichtliches Dashboard mit persönlichen Statistiken
- Anzeige von Statistiken (gruppiert nach: eigene, Mitarbeiter im Gericht, alle Gerichte)
- Schutz persönlicher Daten (nur Anzeige von Nicknames anderer Läufer)
- E-Mail-Benachrichtigung nach mehreren Tagen Inaktivität
- Wöchentliche Herausforderungen
- Festlegung individueller Schrittziele
- E-Mail-Benachrichtigung über wöchentliche Herausforderungen


## Die Anwendung
- [Wer läuft hat Recht](https://mux.ein-toast.de)

## Zugangsdaten
### Für eine/n Gesundheitsmanager/in:
- **E-Mail**: user0@gericht-brb.dummy
- **Passwort**: 12345678

### Für eine/n Standardnutzer/in:
- **E-Mail**: user1@gericht-brb.dummy
- **Passwort**: 12345678

## Das Frontend Repository
- [Frontend Repository](http://github.com/StudyProjectsTHB/WerLaeuftHatRecht_Frontend)

## Die Figma Links
### Für das Desktopdesign:
- [Desktop Design](https://www.figma.com/proto/9slkBlofLbU8UXBxb6RAqe/Wer-l%C3%A4uft-hat-Recht?page-id=0%3A1&node-id=116-741&viewport=184%2C-287%2C0.18&t=RnhLMXGo4LRJhxTL-1&scaling=scale-down&content-scaling=fixed&starting-point-node-id=116%3A741&show-proto-sidebar=1)

### Für das Mobile Design:
- [Mobile Design](https://www.figma.com/proto/9slkBlofLbU8UXBxb6RAqe/Wer-l%C3%A4uft-hat-Recht?page-id=48%3A1150&node-id=168-2654&viewport=184%2C-287%2C0.18&t=EhJoaKeSqwg0OHnM-1&scaling=scale-down&content-scaling=fixed&starting-point-node-id=168%3A2654)

## Für die Ausführung
### Die Enviromentvariablen:
```java
JWT_SECRET_KEY={here_a_secret_key};EMAIL={sender_email};EMAIL_PASSWORD={sender_passwort};WEATHER_KEY={key_of_weather_api}
```
