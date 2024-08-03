import os
import json
import matplotlib.pyplot as plt
import seaborn as sns
import numpy as np
import math
import statistics as sts
import pandas as pd

with open("reversedStats.json", "r") as f:
    stats = json.load(f)

with open("defaultStats.json", "r") as f:
    default = json.load(f)

os.makedirs("./resultGraphs/", exist_ok=True)

graphBounds = {}

numErrorsPerPool = {
    "InfinityPool": 3,
    "LogicErrorPool": 7,
    "MemoryPool": 2,
    "MultiThreadedPool": 3
}

metricCorrectName = {
    "avg" : "average fitness",
    "best" : "best fitness",
    "var": "diversity",
    "duration": "duration",
    "std": "standard deviation",
    "error": "percentage of errors found"
}

hyperParameterCorrectName = {
    "crossoverProp": "crossover probability",
    "crossOverType": "crossover type",
    "mutationProp": "mutation probability",
    "mutationType": "mutation type",
    "numGenerations": "number of generations",
    "reproductionProp": "reproduction probability",
    "selectionMethod": "selection method",
    "tournamentSize": "tournament size",
    "populationSize": "population size"
}

poolCorrectName = {
    "InfinityPool" : "infinity loop error pool",
    "LogicErrorPool": "logical error pool",
    "MemoryPool": "memory error pool",
    "MultiThreadedPool": "concurrent programming error pool"
}

metricAxisName = {
    "avg" : "Average Fitness",
    "best" : "Best Fitness",
    "var": "Diversity",
    "duration": "Duration",
    "std": "Standard Deviation",
    "error": "Percentage of Errors"
}

defaultValues = {
    "reproductionProp": 0.5,
    "crossoverProp": 0.5,
    "mutationType": "BitWise Inversion",
    "populationSize": 50,
    "numGenerations": 50,
    "crossOverType": "OnePoint CrossOver",
    "mutationProp": 0.5,
    "selectionMethod": "Tournament Selection",
    "tournamentSize": 2,
    "LTLWeight": 0.3333333333333333,
    "MWeight": 0.3333333333333333,
    "GWeight": 0.3333333333333333,
}

for pool in stats:
    poolData = stats[pool]

    for hp in poolData:
        hpData = poolData[hp]

        if not hp in graphBounds:
            graphBounds[hp] = {}

        for metric in hpData:
            metricData = hpData[metric]

            avg = default[pool][metric]["avg"]
            std = default[pool][metric]["std"]

            if metric == "error":
                maxAvg = max(avg) / numErrorsPerPool[pool]
                minAvg = min(avg) / numErrorsPerPool[pool]
                maxStd = max(std) / numErrorsPerPool[pool]
            else:
                maxAvg = max(avg)
                minAvg = min(avg)
                maxStd = max(std)

            for param in metricData:
                paramData = metricData[param]
                avg = paramData["avg"]
                std = paramData["std"]

                if metric == "error":
                    tempMaxAvg = max(avg) / numErrorsPerPool[pool]
                    tempMinAvg = min(avg) / numErrorsPerPool[pool]
                    tempMaxStd = max(std) / numErrorsPerPool[pool]
                else:
                    tempMaxAvg = max(avg)
                    tempMinAvg = min(avg)
                    tempMaxStd = max(std)

                maxAvg = max(maxAvg, tempMaxAvg)
                minAvg = min(minAvg, tempMinAvg)
                maxStd = max(maxStd, tempMaxStd)

            if not metric in graphBounds[hp]:
                graphBounds[hp][metric] = {
                    "maxAvg": maxAvg,
                    "minAvg": minAvg,
                    "maxStd": maxStd
                }

            graphBounds[hp][metric] = {
                "maxAvg": max(maxAvg, graphBounds[hp][metric]["maxAvg"]),
                "minAvg": min(minAvg, graphBounds[hp][metric]["minAvg"]),
                "maxStd": max(maxStd, graphBounds[hp][metric]["maxStd"])
            }

for pool in stats:
    os.makedirs("./resultGraphs/{}".format(pool), exist_ok=True)
    poolData = stats[pool]
    for hp in poolData:
        os.makedirs("./resultGraphs/{}/{}/".format(pool, hp), exist_ok=True)
        hpData = poolData[hp]
        for metric in hpData:
            metricData = hpData[metric]

            if (metric == "error"):
                avg = default[pool][metric]["avg"] 
                avg = [x / numErrorsPerPool[pool] for x in avg]
                std = default[pool][metric]["std"] 
                std = [x / numErrorsPerPool[pool] for x in std]
            else:
                avg = default[pool][metric]["avg"]
                std = default[pool][metric]["std"]

            plt.plot(range(0, len(avg)), avg, label=defaultValues[hp])
            plt.fill_between(range(0, len(avg)), [a - s for a, s in zip(avg, std)],
                             [a + s for a, s in zip(avg, std)], alpha=0.2)

            for param in metricData:
                paramData = metricData[param]
                if (metric == "error"):
                    avg = paramData["avg"] 
                    avg = [x / numErrorsPerPool[pool] for x in avg]
                    std = paramData["std"] 
                    std = [x / numErrorsPerPool[pool] for x in std]
                else:
                    avg = paramData["avg"]
                    std = paramData["std"]

                plt.plot(range(0, len(avg)), avg, label=param)
                plt.fill_between(range(0, len(avg)), [a - s for a, s in zip(avg, std)],
                                 [a + s for a, s in zip(avg, std)], alpha=0.2)

            plt.ylim(top=graphBounds[hp][metric]
                     ["maxAvg"] + graphBounds[hp][metric]["maxStd"])
            plt.ylim(bottom=max(graphBounds[hp][metric]
                     ["minAvg"] - graphBounds[hp][metric]["maxStd"], 0))
            plt.xlabel('Generation')
            plt.ylabel('{}'.format(metricAxisName[metric]))
            plt.title(
                'Averages and standard deviations of the {} for the \ndifferent {} hyperparameters for the {} per generation'.format(metricCorrectName[metric], hyperParameterCorrectName[hp], poolCorrectName[pool]))
            
            # Get handles and labels
            handles, labels = plt.gca().get_legend_handles_labels()

            # Sort by labels
            sorted_labels_handles = sorted(zip(labels, handles))
            sorted_labels, sorted_handles = zip(*sorted_labels_handles)

            # Add sorted legend
            plt.legend(sorted_handles, sorted_labels)
            
            #plt.legend()
            plt.tight_layout()

            # Display the plot
            plt.savefig(
                "./resultGraphs/{}/{}/{}_{}_{}.png".format(pool, hp, pool, hp, metric),bbox_inches='tight')
            plt.clf()

poolFinalStats = {}

for pool in stats:
    os.makedirs("./resultTables/{}".format(pool), exist_ok=True)
    poolData = stats[pool]
    for hp in poolData:
        hpData = poolData[hp]
        for metric in hpData:
            if not metric in poolFinalStats:
                os.makedirs("./resultTables/{}".format(metric), exist_ok=True)
                os.makedirs("./resultTables/{}/csv".format(metric), exist_ok=True)
                os.makedirs("./resultTables/{}/latex".format(metric), exist_ok=True)
                os.makedirs("./resultTables/{}/csv/avg".format(metric), exist_ok=True)
                os.makedirs("./resultTables/{}/csv/final".format(metric), exist_ok=True)
                os.makedirs("./resultTables/{}/latex/avg".format(metric), exist_ok=True)
                os.makedirs("./resultTables/{}/latex/final".format(metric), exist_ok=True)
                poolFinalStats[metric] = {
                    "avg" : {},
                    "final": {}
                }
                
            if not hp in poolFinalStats[metric]["avg"]:
                poolFinalStats[metric]["avg"][hp] = {}
                poolFinalStats[metric]["final"][hp] = {}
                
            if not pool in poolFinalStats[metric]["avg"][hp]:
                poolFinalStats[metric]["avg"][hp][pool] = {}
                poolFinalStats[metric]["final"][hp][pool] = {}
            
            metricData = hpData[metric]

            if (metric == "error"):
                avg = default[pool][metric]["avg"] 
                avg = [x / numErrorsPerPool[pool] for x in avg]
                std = default[pool][metric]["std"] 
                std = [x / numErrorsPerPool[pool] for x in std]
            else:
                avg = default[pool][metric]["avg"]
                std = default[pool][metric]["std"]
                
            poolFinalStats[metric]["avg"][hp][pool][defaultValues[hp]] = sts.mean(avg)
            poolFinalStats[metric]["final"][hp][pool][defaultValues[hp]] = avg[-1]

            for param in metricData:
                paramData = metricData[param]
                if (metric == "error"):
                    avg = paramData["avg"] 
                    avg = [x / numErrorsPerPool[pool] for x in avg]
                    std = paramData["std"] 
                    std = [x / numErrorsPerPool[pool] for x in std]
                else:
                    avg = paramData["avg"]
                    std = paramData["std"]

                poolFinalStats[metric]["avg"][hp][pool][param] = sts.mean(avg)
                poolFinalStats[metric]["final"][hp][pool][param] = avg[-1]
                
for metric in poolFinalStats:
    metricData = poolFinalStats[metric]
    for type in metricData:
        typeData = metricData[type]
        for hp in typeData:
            hpData = typeData[hp]
            headers = "Hyperparameter,"
            lines = {}
            for pool in hpData:
                headers += "{},".format(pool)
                poolData = hpData[pool]
                for param in poolData:
                    try:
                        paramName = float(param)
                    except ValueError:
                        paramName = param
                        
                    if not paramName in lines:
                        lines[paramName] = "{},".format(param)
                        
                    paramData = poolData[param]
                    lines[paramName] += "{},".format(paramData)
                
            with open("./resultTables/{}/csv/{}/{}.csv".format(metric,type, hp), "w") as f:
                f.write(headers.rstrip(','))
                f.write("\n")
                sorted_keys = sorted(lines.keys())
                for key in sorted_keys:
                    line = lines[key].rstrip(',')
                    f.write(line)
                    f.write("\n")
                    
            df = pd.read_csv("./resultTables/{}/csv/{}/{}.csv".format(metric,type, hp))
            latex_table = df.to_latex(index=False, float_format="%.8f",)
            with open("./resultTables/{}/latex/{}/{}.tex".format(metric,type, hp), "w") as f:
                latex_table = latex_table.replace("\\begin{tabular}{rrrrrr}", "\\begin{tabular}{|c|c|c|c|c|}")
                latex_table = latex_table.replace("\\\\", "\\\\\\hline")
                f.write(latex_table)