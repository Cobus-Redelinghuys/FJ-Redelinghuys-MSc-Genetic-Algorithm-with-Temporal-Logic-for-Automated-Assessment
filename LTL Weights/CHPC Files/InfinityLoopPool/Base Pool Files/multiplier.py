import json
import os
import shutil
from datetime import datetime
import copy

def copyToFile(param, altValue, baseFolder):
    sourceDir = "./BaseFiles"
    destDir = "./"+baseFolder+"/" + str(param) +"_"+ str(altValue)+"/"
    files = os.listdir(sourceDir)

    if(os.path.exists(destDir)):
        shutil.rmtree(destDir)

    os.makedirs(destDir)

    for file in files:
        sourcePath = os.path.join(sourceDir, file)
        destPath = os.path.join(destDir, file)

        if os.path.isfile(sourcePath):
            shutil.copy(sourcePath, destPath)
        elif os.path.isdir(sourcePath):
            shutil.copytree(sourcePath, destPath)

    with open(destDir + "makefile", "w") as file:
        file.write("main:\n")
        file.write("\tjava -jar GA.jar >> log.txt")

    return destDir

experimentValues = []

for w1 in range(0,101,25):
    for w2 in range(0,101,25):
        for w3 in range(0,101,25):
            if(w1 + w2 + w3 == 100):
                experimentValues += [{
                    "LTLWeight": w1/100,
                    "MWeight": w2/100,
                    "GWeight": w3/100
                }]
                
poolNum = 1
                
with open("./BaseFiles/Config.json", "r") as f:
    defaultValues = json.load(f)
    
defaultValues["LTLWeight"] = 1/3
defaultValues["MWeight"] = 1/3
defaultValues["GWeight"] = 1/3
    
with open("makefile", "w") as f:
    f.write("main:\n")

for EXPType in experimentValues:
    alternativeValues = {}
    filePaths = ""                     

    current_datetime = datetime.now()
    current_datetime_str = current_datetime.strftime("%Y_%m_%d_%H_%M_%S")

    destDir = copyToFile("FF Weight Set",poolNum, current_datetime_str)
    tempConfig = defaultValues.copy()
    for key in EXPType.keys():
        tempConfig[key] = EXPType[key]

    with open(destDir+"/Config.json", "w") as file:
        file.write(json.dumps(tempConfig, indent=4))

    with open("makefile", "a") as file:
        file.write("\tmake -C " + destDir + " || true \n")
    
    poolNum += 1
    
destDir = copyToFile("default","",current_datetime_str)
with open(destDir+"/Config.json", "w") as file:
    file.write(json.dumps(defaultValues.copy(), indent=4))
            
with open("makefile", "a") as file:
    file.write("\tmake -C " + destDir + " || true \n")
    
latex_table = r'''\begin{table}[h!]
\centering
\begin{tabular}{|c|c|c|c|}
\hline
Pool Number & LTL Weight & M Weight & G Weight \\
\hline
'''

# Adding the data rows
for i, experiment in enumerate(experimentValues):
    latex_table += f"{i+1} & {experiment['LTLWeight']} & {experiment['MWeight']} & {experiment['GWeight']} \\\\ \n"
    latex_table += r'\hline' + '\n'

# Closing the table
latex_table += r'''\end{tabular}
\caption{Experiment Weights}
\end{table}
'''

# Write the table to a file
with open("table.tex", "w") as file:
    file.write(latex_table)