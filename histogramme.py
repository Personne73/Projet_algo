# -*- coding: latin-1 -*-
"""
Le fichier des distances relatives dont vous voulez afficher l'histogramme contient
une distance relative par ligne et rien d'autre.
Exemple :
0.494520822486
0.0939254807197
0.365846578605
0.150577932504
...

Dans ce qui suit, ce fichier est DR.CSV   (CSV = comma separated values)

Ouvrir un terminal Unix ou Linux et se placer dans le rï¿½pertoire qui contient
le fichier DR.CSV

Taper la ligne de commande
   python3 histogramme.py DR
ou
   python histogramme.py DR
(respectivement Python version 3 et Python version 2)

Vous obtiendrez un fichier image
   DR.PNG
contenant l'histogramme.

Vous pouvez donner le nom de fichier que vous voulez.

Exemple : si le fichier DR_PROBLEME_TRUC_MACHIN.CSV contient les distances relatives
du problï¿½me truc machin, la ligne de commande
	python3 histogramme.py DR_PROBLEME_TRUC_MACHIN
gï¿½nï¿½re un fichier image
    DR_PROBLEME_TRUC_MACHIN.PNG
contenant l'histogramme.

Exemple d'exï¿½cution dans un terminal Unix :
% python histogramme.py DR_PROBLEME_TRUC_MACHIN
l'histogramme est dans le fichier DR_PROBLEME_TRUC_MACHIN.PNG
%

"""
import csv
import matplotlib.pyplot as plt


def histogramme(filename, title):
    DR = []  # distances relatives
    with open(filename + ".CSV") as csvfile:
        reader = csv.reader(csvfile)
        for row in reader:
            dr = row[0]
            DR.append(float(dr))
    plt.ylim(0, 15)  # modification suggÃ©rÃ©e par le prof
    #plt.xlim(-0.01, 0.25)
    plt.title("Histogramme des distances relatives des solutions\noptimale et gloutonne " + title)
    h = plt.hist(DR, bins=len(DR))
    plt.savefig(filename + ".PNG")
    plt.close()
    csvfile.close()


def main():
    filename = "Chemin_de_somme_maximum"
    histogramme(filename, "du chemin de somme maximum")
    print("l'histogramme est dans le fichier " + filename + ".PNG")

    filename = "Chemin_du_petit_robot"
    histogramme(filename, "du chemin du petit robot")
    print("l'histogramme est dans le fichier " + filename + ".PNG")

    filename = "Sac_valeur_maximum_glouton_valeur"
    histogramme(filename, "du sac (glouton par valeur)")
    print("l'histogramme est dans le fichier " + filename + ".PNG")

    filename = "Sac_valeur_maximum_glouton_densite_valeur"
    histogramme(filename, "du sac (glouton par densité valeur)")
    print("l'histogramme est dans le fichier " + filename + ".PNG")

    filename = "Repartition_stock_entrepots"
    histogramme(filename, "de la répartition d'un stock")
    print("l'histogramme est dans le fichier " + filename + ".PNG")

    filename = "Repartition_unites_heures"
    histogramme(filename, "de la répartition d'un temps de travail")
    print("l'histogramme est dans le fichier " + filename + ".PNG")


main()
