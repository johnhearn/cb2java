from net.sf.cb2java.copybook import *
from java.io import FileReader
from java.io import FileInputStream
import sys

def recurse(d):
    if d.leaf:
        print d.name + ':' + str(d)
    else:
        print d.name
        for c in d.children():
            recurse(c)

copy = sys.argv[1]
data = sys.argv[2]

copybook = CopybookParser.parse(None, FileReader(copy))
stream = FileInputStream(data)

record = copybook.parseData(stream)
for d in record.children:
    recurse(d)
