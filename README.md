wikipedia
=========

Parsing of useful data from Wikipedia, DBPedia and Freebase 

SectionHeaders
=========

Vystup:
Vystupom programu je zoznam section headrov zo vstupneho suboru. Kazdy section header vo vystupnom subore obsahuje hodnoty documentovej frekvencie, co je pomer vyskytu aktualneho section headru k celkovemu poctu clankov vo vstupnom subore.
Dalsia cas vystupu obsahuje zoznam section headrov, ktore mozu byt sucastou inych section headrov aj s poctom ich vyskytov v inych headroch.

Pouzitie:
1. pri spusteni je potrebne zadat cestu ku vstupnemu suboru clankov anglickej wikipedie(moze byt typu .xml alebo .bz2)
	napr. "C:\wiki\input.xml" alebo "data\input.xml" ak je subor ulozeny v zlozke data 
2. nasledne je potrebne zadat cestu ku vstupnemu suboru 
	napr. "C:\wiki\output.xml" alebo "data\input.xml" a subor sa ulozi do zlozky data
3. nasledne je potrebne zadat kolko section headrov a kolko vyskytov v inych section headroch sa vypise do vystupneho suboru 
	napr. hodnota 10 vypise 10 najcastejsich section headrov a ich frekvencie vyskytu a 10 section headrov, ktore sa najcastejsie vyskytuju v inych section headroch.
	Ak nieje zadany pocet, vypisu sa do vystupneho suboru vsetky.