#!/usr/bin/python3
# GenomeARTIST.py 
# Simona

from tkinter import *
from tkinter import ttk      
from tkinter import filedialog  
import subprocess
import os

class GenomeGUI:
    def __init__(self, master):    

        self.frame_header = ttk.LabelFrame(master)
        
        self.frame_content = ttk.Frame(master)
        self.frame_content.grid(row=1, column=1)
        self.frame_content.config(height = 100, width = 200)
        self.frame_content.config(relief = RIDGE)
        self.frame_content.config(padding = (100, 15))
    
        self.logo = PhotoImage(file = 'artist36b.png')
        ttk.Label(self.frame_content, image = self.logo).grid(row=1, column=1, sticky="E", columnspan=4, pady=10)

        ttk.Label(self.frame_content, text = 'Running Folder:').grid(row=2, column=1,sticky='W',pady=15, padx=10)
        self.pwd = os.getcwd()
        self.runningFolderEntry = ttk.Entry(self.frame_content, width = 60)
        self.runningFolderEntry.insert(0, self.pwd)
        self.runningFolderEntry.grid(row=2, column=2, padx=15)

        self.labelFrame = ttk.LabelFrame(self.frame_content, height = 100, width = 200, text = 'Genome ARTIST Running Parameters')
        self.labelFrame.grid(row=3, column=1, columnspan=2, rowspan=2, sticky='W', padx=10)
        ttk.Label(self.labelFrame, text = 'Open Session in Folder:').grid(row=1, column=1,sticky='W', padx=10)
        self.newFolderEntry = ttk.Entry(self.labelFrame, width = 14)
        self.newFolderEntry.grid(row=1, column=2, padx=10, pady=10)

        ttk.Label(self.labelFrame, text = 'Number of Threads:').grid(row=2, column=1,sticky='W', pady=10, padx=10)
        self.noThreadsEntry = ttk.Entry(self.labelFrame, width = 14)
        self.noThreadsEntry.grid(row=2, column=2, pady=10)


        ttk.Label(self.labelFrame, text = 'Length for Copy from File:').grid(row=1, column=3,sticky='W', padx=10)
        self.copyLenEntry = ttk.Entry(self.labelFrame, width = 14)
        self.copyLenEntry.grid(row=1, column=4, padx=10)

        #ttk.Label(self.labelFrame, text = 'Output Query Search Steps to a File:').grid(row=2, column=3,sticky='W')
        #self.copyLenEntry = ttk.Entry(self.labelFrame, width = 14)
        self.var1 = BooleanVar()
        self.t1 = Checkbutton(self.labelFrame, text="Output Search Steps to a File", variable=self.var1, command=self.setFileOutput)
        self.t1.grid(row=2, column=3, pady=10)

        self.browseButton = ttk.Button(self.frame_content, text = 'Choose Folder', command = self.browseCallback)
        self.submitButton = ttk.Button(self.frame_content, text = 'Submit', command = self.submitCallback)
        self.clearButton = ttk.Button(self.frame_content, text = 'Clear', command = self.clearCallback)
        #self.closeButton = ttk.Button(self.frame_content, text = 'Close', command = self.closeCallback)

        self.browseButton.grid(row=2, column=3)
        self.submitButton.grid(row=3, column=3)
        self.clearButton.grid(row=4, column=3)
        #self.closeButton.grid(row=3, column=3)

    def browseCallback(self):
        self.runningFolderEntry.delete(0, 'end')
        current_directory = filedialog.askdirectory()     
        # Change label contents
        self.runningFolderEntry.insert(0, current_directory)
      

    def setFileOutput(self):
        if (self.var1.get() == True):
            os.environ["FILEOUTPUT"] = "1"
        else:
            os.environ["FILEOUTPUT"] = "0"

    def submitCallback(self):
        self.getFolder=self.runningFolderEntry.get()
        self.newFolder=self.newFolderEntry.get()
        self.noThreads=self.noThreadsEntry.get()
        self.copyLen = self.copyLenEntry.get()

        command = self.getFolder + '/Genome-ARTIST.sh'
        os.environ["NOTHREADS"] = self.noThreads
        os.environ["COPYLENGTH"] = self.copyLen


        if (self.var1.get() == True):
            os.environ["FILEOUTPUT"] = "1"
        else:
            os.environ["FILEOUTPUT"] = "0"

        self.p = subprocess.Popen([command, self.newFolder], cwd=self.getFolder)
        #self.p.wait()


    def clearCallback(self):
        #self.runningFolderEntry.delete(0, 'end')
        self.newFolderEntry.delete(0, 'end')
        self.noThreadsEntry.delete(0, 'end')
        self.copyLenEntry.delete(0, 'end')

    def closeCallback(self):
        #self.p.kill()
        self.quit()

def main():            
    
    root = Tk()
    root.title("Genome ARTIST")
    feedback = GenomeGUI(root)
    root.mainloop()
    
if __name__ == "__main__": main()

