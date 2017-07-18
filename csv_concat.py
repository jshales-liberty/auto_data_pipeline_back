import os
import re
os.chdir("C:/Users/n0236074/Cab_Data/cabspottingdata")
fout = open("cab_data_comb.csv", "w")
for filename in os.listdir(directory):
	prog = re.compile((?<=new_)(.*)(?=.txt))
    result = prog.match(filename)
	for line in open(filename):
    	fout.write(result + "\t" + line)
fout.close()
     	
  
