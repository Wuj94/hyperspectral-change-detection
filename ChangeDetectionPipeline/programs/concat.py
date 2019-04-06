# coding: utf-8
import sys

with open(sys.argv[1], 'r') as f:
    fileA = f.read()
    
with open(sys.argv[2],'r') as f:
    fileB = f.read()
    
with open(sys.argv[3], 'w') as f:
    f.write(fileA)
    f.write(fileB)
    
