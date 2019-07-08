# AlgoPlus

To use this app simply install java JDK in your machine, and open this project with eclipse and run it.
P.S : Instructions always begin with uppercase letter
You can get a build exmaple from : http://algoplus.ga

# What this app can do ?
This is an example of what you can do : 


```
Fonction add(x:Entier, y:Entier):Entier
Debut
	Retourne(x+y);
FinFonction

Algorithme Untitled0
Variables
	x,y,res,i:Entier;
	nom:Chaine;
Debut
	Ecrire("Insert your name : ");
	Lire(nom);
	Ecrire("Hey ",nom," Insert 2 variables : ");
	Lire(x,y);
	res<-x+y;
	Ecrire("Loop Example");
	Pour i Allant de 1 a 10 Faire
		Ecrire(i,":   ",x,'+',y,"=",res);
		Ecrire('\n');
	FinPour
	
Fin
```
If you want to change those instructions you can simply go to : /src/compiler/lexical/TokensWords.java and edit them.

If you want to integrate this in a real project or need some help you can contact me by email : agurram2013@gmail.com
Btw you should consider using other compiler algorithms if you want to get better results like : YACC or LEX, in this example I used my own algorithm.
