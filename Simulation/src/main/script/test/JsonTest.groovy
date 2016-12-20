import groovy.json.JsonOutput
import groovy.json.JsonSlurper

def slurper=new JsonSlurper()
def obj=slurper.parseText """
{
"name":"hx",
"age":24,
"hobby":"java"
}
"""
assert obj.name=='hx'
assert obj.age==24
assert obj.hobby=='java'

def person=[name:'hx',age:24,hobby:'java']
def out=new JsonOutput()
def personJson=out.toJson(person)
assert personJson=='{"name":"hx","age":24,"hobby":"java"}'
