import csv
from datetime import datetime as dt

ratings = {}

with open("data/customer_ratings.csv", encoding="utf8") as file:

    reader = csv.reader(file, delimiter='\t')

    rowcount = 0

    for row in reader:
        
        if(not row[1] in ratings):
            
            ratings[row[1]] = []

            ratings[row[1]].append([row[1],row[2],row[3],row[4][2:]])
            
        else:
            
            addNewEntry = True

            count = 0
            
            for rating in ratings[row[1]]:

                if(rating[3] == row[4][2:]): # 
                    addNewEntry = False
                    
                    if(rating[2] < row[3]) :
                        
                        ratings[row[1]][count] = [row[1],row[2],row[3],row[4][2:]]

                count += 1
                    

            if addNewEntry:

                ratings[row[1]].append([row[1],row[2],row[3],row[4][2:]])

file.close()

with open("cleaned_customer_ratings.csv", "w+", encoding="utf8", newline="") as output:

    writer = csv.writer(output)

    #writer.writerow(["customerID", "Rating","Date","Title ID"])

    for customer in ratings.values():

        for element in customer:

            writer.writerow(element)

output.close()