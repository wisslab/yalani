#!/usr/bin/env python
# coding: utf-8

# ## Logik
# 
# Ein Node hat entweder Kinder (dann ist er ein innerer Knoten) oder keine (dann ist er ein Blatt)
# 
# Wenn Kinder: Pfad verlängern, dort weitersuchen
# Wenn keine: Pfad um eigene Bezeichnung verlängern, zu eigener Notation den Pfad speichern
# 

# In[24]:


import xml.etree.ElementTree as ET
tree = ET.parse('Downloads/rvko_2020_1.xml')

result = []


def do_it(path, element):

    if element.findall('./children/node'):
        for child in element.findall('./children/node'):
                if path == "":
                    do_it(element.attrib['benennung'], child)
                else:
                    do_it(element.attrib['benennung'] + " / " + path, child)
    else:
        result.append(element.attrib['notation']+ "\t" + element.attrib['benennung'] + " / " + path)

for element in tree.findall('./node'):
    do_it("", element)

with open ("Downloads/rvko_2020_1.strings", "w", encoding="UTF-8") as r_file:
    r_file.write("\n".join(result))

