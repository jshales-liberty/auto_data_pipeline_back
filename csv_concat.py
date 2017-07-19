import os
import csv
directory = "C:/Users/n0236074/Cab_Data/cabspottingdata"
dir_path = os.path.dirname(os.path.realpath(__file__))
os.chdir(directory)


# with open('cab_data_combine.csv', 'w+') as csvfile:
# 	cabwriter = csv.writer(csvfile, delimiter=",")
# 	cabwriter.writerow('id,lati,longi,status,timestamp')
# 	for filename in os.listdir(directory):
# 		i=i+1
# 		for line in open(filename):
# 			cabwriter.writerow(str(i) + "," + line.replace(" ",","))


fout = open("cab_data_comb.csv", "w+")
##fout.write('"id","lati","longi","status","timestamp\r"')
i = 0
for filename in os.listdir(directory):
	i=i+1
	for line in open(filename):
		fout.write(str(i) + "," + line.replace(" ",","))
fout.close()
os.chdir(dir_path)
     	
  
