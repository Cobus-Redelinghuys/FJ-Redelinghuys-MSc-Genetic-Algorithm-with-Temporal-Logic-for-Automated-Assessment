import json
import os
import shutil
import subprocess
import statistics

rawStats = {}
errorTypes = ["InfinityPool", "LogicErrorPool",
              "MemoryPool", "MultiThreadedPool"]


def list_directories_sub(path):
    for root, dirs, files in os.walk(path):
        for directory in dirs:
            print(os.path.join(root, directory))


def list_folders(directory) -> [str]:
    result = []
    with os.scandir(directory) as entries:
        for entry in entries:
            if entry.is_dir():
                result += [entry.name]

    return result


def run_script(script_path):
    # Get the absolute path of the script
    abs_script_path = os.path.abspath(script_path)
    # Run the script
    result = subprocess.run(['python', abs_script_path],
                            capture_output=True, text=True)

    # Print the output and any errors
    print('Output:')
    print(result.stdout)
    # if result.stderr:
    #    print('Errors:')
    #    print(result.stderr)


def copy_file(src, dst):
    # Ensure the destination directory exists
    os.makedirs(os.path.dirname(dst), exist_ok=True)
    # Copy the file
    shutil.copy2(src, dst)
    print(f'File copied from {src} to {dst}')


current_directory = os.getcwd()
for errorType in errorTypes:
    rawStats[errorType] = {}
    for i in range(1, 11):
        rawStats[errorType][i] = {}
        path = "./{}/Pool{}/".format(errorType, i)
        folders = list_folders(path)
        dates = []
        for folder in folders:
            if "2024" in folder:
                dates += [folder]
        list.sort(dates)
        date = dates[-1]
        fullPath = "{}/{}/".format(path, date)

        copy_file("./StatsGeneratorFile/groupStats.py", fullPath)
        os.chdir(fullPath)
        run_script("./groupStats.py".format(fullPath))
        with open("./cumulative_stats.json", "r") as f:
            rawStats[errorType][i] = json.load(f)
        os.chdir(current_directory)

with open("./rawStats.json", "w") as f:
    f.write(json.dumps(rawStats, indent=4))

summaryStats = {}

for errorType in errorTypes:
    summaryStats[errorType] = {}
    for i in range(1, 11):
        errorData = rawStats[errorType][i]
        for hp in errorData:
            if not hp in summaryStats[errorType]:
                summaryStats[errorType][hp] = {}

            hpData = errorData[hp]

            for param in hpData:
                if not param in summaryStats[errorType][hp]:
                    summaryStats[errorType][hp][param] = {}

                paramData = hpData[param]

                for metric in paramData:
                    if not metric in summaryStats[errorType][hp][param]:
                        summaryStats[errorType][hp][param][metric] = []

                    metricData = paramData[metric]
                    summaryStats[errorType][hp][param][metric] += [metricData]

with open("summaryStats.json", "w") as f:
    f.write(json.dumps(summaryStats, indent=4))

processedSummary = {}
for errorType in errorTypes:
    processedSummary[errorType] = {}
    errorData = summaryStats[errorType]
    for hp in errorData:
        if not hp in processedSummary[errorType]:
            processedSummary[errorType][hp] = {}

        hpData = errorData[hp]

        for param in hpData:
            if not param in processedSummary[errorType][hp]:
                processedSummary[errorType][hp][param] = {}

            paramData = hpData[param]

            for metric in paramData:
                if not metric in processedSummary[errorType][hp][param]:
                    processedSummary[errorType][hp][param][metric] = [
                        [0] * len(paramData[metric]) for _ in range(len(paramData[metric][0]))]

                metricData = paramData[metric]
                arr = [[0] * len(paramData[metric][0])
                       for _ in range(len(paramData[metric]))]
                i = 0
                for pool in metricData:
                    for j in range(0, len(pool)):
                        processedSummary[errorType][hp][param][metric][j][i] = pool[j]
                    i += 1

                n = len(metricData)
                temp = {
                    "avg": [],
                    "std": []
                }
                for i in range(0, len(processedSummary[errorType][hp][param][metric])):
                    temp["avg"] += [statistics.mean(
                        processedSummary[errorType][hp][param][metric][i])]
                    temp["std"] += [statistics.stdev(
                        processedSummary[errorType][hp][param][metric][i])]

                processedSummary[errorType][hp][param][metric] = temp

with open("processedStats.json", "w") as f:
    f.write(json.dumps(processedSummary, indent=4))

reversedStats = {}

defaultStats = {}
for pool in processedSummary:
    reversedStats[pool] = {}
    poolData = processedSummary[pool]
    for hp in poolData:
        #reversedStats[pool][hp] = {}
        hpData = poolData[hp]
        data = {}

        for param in hpData:
            paramData = hpData[param]
            for metric in paramData:
                if not metric in data:
                    data[metric] = {}

                if not param in data[metric]:
                    data[metric][param] = {}

                metricData = paramData[metric]

                avg = metricData["avg"]
                std = metricData["std"]
                if "default" in hp:
                    if not pool in defaultStats:
                        defaultStats[pool] = {}
                    defaultStats[pool][metric] = {"avg": avg,
                                                  "std": std}
                else:
                    data[metric][param]["avg"] = avg
                    data[metric][param]["std"] = std

        if not "default" in hp:
            reversedStats[pool][hp] = data


with open("reversedStats.json", "w") as f:
    f.write(json.dumps(reversedStats, indent=4))

with open("defaultStats.json", "w") as f:
    f.write(json.dumps(defaultStats, indent=4))