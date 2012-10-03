from javax.swing import *
from java.lang import Integer
from java.lang import String
from net.sf.cb2java.copybook import *
from java.awt import GridLayout
from java.awt import FlowLayout
from java.io import FileReader
from java.io import OutputStream
from java.awt.event import MouseEvent
from javax.swing import WindowConstants
from listeners import *

class Text(JTextField):
    def setinput(self, stream):
        b = ByteBuffer()
        b.fill(stream)
        self.setbytes(b.get())

    def setbytes(self, bytes):
        self.settext(String(bytes))

    def settext(self, string):
        self.setText(string)

    def gettext(self):
        return self.getText()

    def setparent(self, p):
        self.window = p

class Buffer(OutputStream):
    def __init__(self):
        self.bytes = []
        
    def write(self, b):
        self.bytes += b

class Hex(Text):
    def __init__(self):
        self.addMouseListener(MouseClickEvent(self.detail))
        self.dialog = None

    def detail(self, e):
        if e.getButton() == MouseEvent.BUTTON3 and self.dialog == None:
            self.dialog = BitDialog(self.window.frame, self.bytes)
            self.dialog.addWindowListener(WindowCloseListener(lambda e: self.dialogclosed()))

    def dialogclosed(self):
        self.dialog = None
    
    def setbytes(self, bytes):
        self.bytes = bytes
        h = ""
        for b in bytes:
            h += "%(value)02X" % {"value" : (0xFF & b)} + ' '
        self.settext(h[:-1])

class Data:
    def __init__(self, c, p):
        self.copybook = c
        self.panel = p
        self.window = p.window

    def setinput(self, stream):
        self.record = self.copybook.parseData(stream)
        for d in self.record.children:
            self.recurse(d)

    def setoutput(self, stream):
        self.record.write(stream)

    def createnew(self):
        self.record = self.copybook.createNew()
        for d in self.record.children:
            self.recurse(d)

    def recurse(self, g):
        for d in g.children:
            if d.leaf:
                l = JLabel(d.name)
                t = Text()
                t.setparent(self.window)
                h = Hex()
                h.setparent(self.window)
                t.addActionListener(ActionEventListener(Binder(t, d, h).datachange))
                self.panel + Mixed(l, t, h)
                b = Buffer()
                d.write(b)
                h.setbytes(b.bytes)
                t.settext(str(d))
            else:
                self.recurse(d)

class Binder:
    def __init__(self, t, d, h):
        self.t = t
        self.d = d
        self.h = h

    def datachange(self, e):
        self.d.setValue(self.t.text)
        b = Buffer()
        self.d.write(b)
        self.h.setbytes(b.bytes)
        

class Mixed(JPanel):
    def __init__(self, l, d, h):
        self.setLayout(GridLayout(3,1))
        self + l
        self + d
        self + h
        
    def __add__(self, c):
        self.add(c)
        return self

class Panel(JPanel):
    def __init__(self):
        self.setLayout(FlowLayout(FlowLayout.LEFT))

    def setcopy(self, s):
        self.copy = CopybookParser.parse(None, FileReader(s))

    def setinput(self, stream):
        self.data = Data(self.copy, self)
        self.data.setinput(stream)

    def setoutput(self, stream):
        self.data.setoutput(stream)

    def createnew(self):
        self.data = Data(self.copy, self)
        self.data.createnew()

    def setparent(self, w):
        self.window = w

    def __add__(self, c):
        self.add(c)
        return self

class BitDialog(JDialog):
    def __init__(self, w, b):
        JDialog.__init__(self, w)
        self.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE)
        self.panel = Panel()
        self.getContentPane().add(self.panel)
        t = Text()
        t.addFocusListener(FocusLostListener(self.lostfocus))
        self.panel + t
        view = ""
        for byte in b:
            view += Integer.toString((0xFF & byte), 2).zfill(8)
            view += ' '
        t.settext(view[:-1])
        self.pack()
        self.setVisible(1)

    def lostfocus(self, e):
        print "dialog lost focus"
        self.dispose()
