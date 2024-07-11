import os
import shutil
import json


seeds = [0, 2264295202, 916492038, 2578164856, 32034829, 2078873889, 1929610041, 1846197244, 554488706, 3472689356]
# Source directory
source_dir = 'Base Pool Files'

# Ensure the source directory exists
if not os.path.exists(source_dir):
    print(f"Source directory '{source_dir}' does not exist.")
    exit(1)

# Create and copy contents to target directories
for i in range(1, 11):
    target_dir = f'Pool{i}'
    
    # Create the target directory if it does not exist
    if not os.path.exists(target_dir):
        os.makedirs(target_dir)
    
    # Copy all contents from source to target directory
    for item in os.listdir(source_dir):
        source_item = os.path.join(source_dir, item)
        target_item = os.path.join(target_dir, item)
        
        # Check if it is a file or a directory
        if os.path.isdir(source_item):
            shutil.copytree(source_item, target_item, dirs_exist_ok=True)
        else:
            shutil.copy2(source_item, target_item)
            
    with open(target_dir+"/BaseFiles/Config.json", "r") as f:
        config = json.load(f)
        
    config["seed"] = seeds[i-1]
    
    with open(target_dir+"/BaseFiles/Config.json", "w") as f:
        f.write(json.dumps(config, indent=4))
        
    with open(target_dir+"/MultiThreadedPool.job", "r") as f:
        lines = f.readlines()
        
    lines[5] = "#PBS -o /mnt/lustre/users/fredelinghuys/Pools/MultiThreadedPool/Pool{}/MultiThreadedPool{}.out\n".format(i,i)
    lines[6] = "#PBS -e /mnt/lustre/users/fredelinghuys/Pools/MultiThreadedPool/Pool{}/MultiThreadedPool{}.err\n".format(i,i)
    lines[11] = "cd /mnt/lustre/users/fredelinghuys/Pools/MultiThreadedPool/Pool{}/\n".format(i)
    
    with open(target_dir+"/MultiThreadedPool.job", "w") as f:
        f.writelines(lines)
        
    os.rename(target_dir+"/MultiThreadedPool.job", target_dir+"/MultiThreadedPool{}.job".format(i))
    
print("All contents have been successfully copied.")
