from javax.swing import JFrame
from java.awt import GridLayout
from javax.swing import WindowConstants
from javax.swing import JMenuBar
from javax.swing import JMenu
from javax.swing import JMenuItem
from javax.swing import JFileChooser
from java.io import FileInputStream
from java.io import FileOutputStream
import pickle
from listeners import *

class Window:
    def __init__(self, text):
        self.frame = JFrame()
        self.frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE)
        self.frame.getContentPane().setLayout(GridLayout(1,1))
        self.settings = Settings()
        self.text = text
        self + text
        m = JMenuBar()
        self.menubar = m
        self.filemenu = self.menu()
        m.add(self.filemenu)
        self.frame.setJMenuBar(self.menubar)
        self.frame.setSize(1000, 600)
        self.frame.setVisible(1)

    def menu(self):
        f = JMenu("File")

        o = JMenuItem("Copy")
        o.addActionListener(ActionEventListener(self.opencopy))
        self.open = o
        f.add(o)

        o = JMenuItem("New")
        o.addActionListener(ActionEventListener(self.new))
        self.open = o
        f.add(o)

        o = JMenuItem("Open")
        o.addActionListener(ActionEventListener(self.opendialog))
        self.open = o
        f.add(o)

        o = JMenuItem("Save")
        o.addActionListener(ActionEventListener(self.save))
        self.open = o
        f.add(o)

        o = JMenuItem("Save As")
        o.addActionListener(ActionEventListener(self.saveas))
        self.open = o
        f.add(o)

        return f

    def __add__(self, c):
        self.frame.getContentPane().add(c)
        return self

    def opencopy(self, e):
        c = None
        if "lastcopy" in self.settings.keys():
            c = JFileChooser(self.settings["lastcopy"])
        else:
            c = JFileChooser()
            
        r = c.showOpenDialog(self.frame)
        
        if r == JFileChooser.APPROVE_OPTION:
            f = c.getSelectedFile()
            self.f = f
            s = f.getAbsolutePath()

            self.settings["lastcopy"] = f.getParent()
            self.settings.save()
            
#            i = FileInputStream(s)
            self.text.setcopy(s)

    def opendialog(self, e):
        c = None
        if "lastdir" in self.settings.keys():
            c = JFileChooser(self.settings["lastdir"])
        else:
            c = JFileChooser()
            
        r = c.showOpenDialog(self.frame)
        
        if r == JFileChooser.APPROVE_OPTION:
            f = c.getSelectedFile()
            self.f = f
            s = f.getAbsolutePath()

            self.settings["lastdir"] = f.getParent()
            self.settings.save()
            
            i = FileInputStream(s)
            self.text.setinput(i)
            self.frame.pack()
            self.frame.setSize(800, 500)

    def new (self, e):
        self.text.createnew()
        self.frame.pack()
        self.frame.setSize(800, 500)

    def save(self, e):
        o = FileOutputStream(self.f)
        self.text.setoutput(o)

    def saveas(self, e):
        c = None
        if "lastdir" in self.settings.keys():
            c = JFileChooser(self.settings["lastdir"])
        else:
            c = JFileChooser()
            
        r = c.showOpenDialog(self.frame)
        
        if r == JFileChooser.APPROVE_OPTION:
            f = c.getSelectedFile()
            self.f = f
            s = f.getAbsolutePath()

            self.settings["lastdir"] = f.getParent()
            self.settings.save()
            
            o = FileOutputStream(s)
            self.text.setoutput(o)
            
class Settings:
    def __init__(self):
        self.values = {}
        try:
            f = open("settings.props", 'r')

            for line in f:
                print line
                s = line.split('=')
                self.values[s[0]]=s[1].strip()
        except IOError:
            print "file not found"

    def keys(self):
        return self.values.keys()

    def __setitem__(self, k, v):
        self.values[k]=v

    def __getitem__(self, k):
        return self.values[k]

    def save(self):
        f = open("settings.props", 'w')
        for k in self.values.keys():
            f.write(k + '=' + self.values[k] + '\n')
        f.close()

