from java.awt.event import MouseAdapter
from java.awt.event import WindowAdapter
from java.awt.event import ActionListener
from java.awt.event import FocusAdapter
from java.awt.event import TextListener

class MouseClickEvent(MouseAdapter):
    def __init__(self, f):
        self.func = f

    def mouseClicked(self, e):
        self.func(e)

class WindowCloseListener(WindowAdapter):
    def __init__(self, f):
        self.func = f

    def windowClosed(self, e):
        self.func(e)

class ActionEventListener(ActionListener):
    def __init__(self, f):
        self.f = f

    def actionPerformed(self, e):
        self.f(e)

class FocusLostListener(FocusAdapter):
    def __init__(self, f):
        self.f = f

    def focusLost(self, e):
        self.f(e)
        
class FocusGainedListener(FocusAdapter):
    def __init__(self, f):
        self.f = f

    def focusGained(self, e):
        self.f(e)

class TextChangeListener(TextListener):
    def __init__(self, f):
        self.f = f

    def textValueChanged(self, e):
        self.f(e)
