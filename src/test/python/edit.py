from text import *
from window import Window
from net.sf.cb2java.copybook import *
from java.io import FileReader

c = CopybookParser.parse("simple", FileReader("C:/code/copybook/simple.cpy"))

#h = Data(c)
p = Panel()
w = Window(p)
p.setparent(w)
