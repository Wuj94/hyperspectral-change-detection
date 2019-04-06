# coding: utf-8
import scipy.io as sio
import sys

"""The script takes as arguments 
-1: the .mat data set file path at a time point 
-2: the name of the matrix containing data
-3: 0 if the data set is at t0 time point 1 if the data set is at t1 time point
-4: output file path"""

input_file = sys.argv[1]
name = sys.argv[2]
mode = sys.argv[3]
out_filepath = sys.argv[4]

mat = sio.loadmat(input_file)
mat = mat[name]
file_string = []
for y in range(mat.shape[0]):
    for x in range(mat.shape[1]):
        file_string.append((str(x) + ' ' + str(y) + ' ' + str(mat[y,x,:]).replace('[', '').replace(']', '').replace('\n', '')).replace('  ', ' ') + '\n')
        
        
output = ''
for i in range(len(file_string)):
    output += file_string[i]
    
m = '' #mode
if mode == '0':
	m = 'w'
elif mode == '1':
	m = 'a'
with open(out_filepath, m) as f:
    f.write(output)
