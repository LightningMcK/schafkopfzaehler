# Schafkopfzaehler - Hinweise

## Anleitung OpenCV mit SIFT-Operator 

Für die Kartenerkennung durch den Server wird der SIFT-Operator benötigt. Dabei ist zu beachten, dass dieser in der neusten OpenCV-Version
nicht frei verfügbar ist. Aus diesem Grund muss die Version 3.4.2.17 ausgewählt werden.
Dementsprechend muss man unter folgendem Link die passende OpenCV_contrib_python Wheel Datei, entsprechend des Betriebssystem und der Python Umgebung, 
heruntergeladen: https://pypi.org/project/opencv-contrib-python/3.4.2.17/#files 

Anschließend kann man über folgenden Befehl OpenCV in seiner Python Environment installieren: 
pip3 install pfad_zur_wheel_datei 

## Server-URL in der App

Zum korrekten Funktion der App wird in diesem Stadium des Projekts noch ein händisches Eintragen der Server-URL benötigt. Diese muss in der
CardCaptureActivity eingetragen werden. Der korrekte Pfad zum Hochladen und Erkennen der Datei muss nicht verändert werden, solang man den
bereitgestellten Server im Kartenerkennung_FINAL.rar Verzeichnis nimmt. 
