from pathlib import Path
import os

from pack import compress
import shutil

import logging

cwd = Path('')
basedir = cwd.absolute().parent
snapdir = basedir.joinpath('snapTest')
outdir = cwd.joinpath('out')
logging.basicConfig(level=logging.INFO)


def clean():
    for f in cwd.iterdir():
        name = str(f.name)
        if 'cache' in name.lower():
            for ff in f.iterdir():
                os.remove(str(ff))
                print('deleting {}'.format(str(ff)))
            f.rmdir()

def prepare():
    if not outdir.exists():
        outdir.mkdir()


def compress_snap():
    files=['block.js','blockconfig.js','amplifier.js','homopoly.js','integrator.js','line.js','model.js']
    jsfiles = [str(snapdir.joinpath(file)) for file in files]
    js_out_file = str(outdir.joinpath('snap-util.js'))
    compress(jsfiles, outfile=js_out_file)

def publish():
    'copy compressed js to project js/util folder'
    for f in outdir.iterdir():
        if f.is_file:
            logging.info('publishing %s', str(f))
            js_pub_dir = basedir.joinpath('Simulation/src/main/webapp/js/util')
            shutil.copy(f, js_pub_dir)

def copy_js_src():
    def accept(p):
        files = ['jquery-3.1.1.min.js', 'snap.svg-min.js', '.html']
        return not any([f in p.name for f in files])

    def copy(p):
        js_src_dir = basedir.joinpath('Simulation/src/main/webapp/js/src')
        shutil.copy(p, js_src_dir)

    file_filter(snapdir, accept, copy)

def file_filter(base, accept, callback):
    'if accpect(base as path), callback(base) recursively'
    if type(base) == str:
        p = Path(base)
    else:
        p = base
    if p.is_file():
        if accept(p):
            logging.info('{} {}'.format(callback.__name__, p.name))
            return callback(p)
    elif p.is_dir():
        for f in p.iterdir():
            file_filter(f, accept, callback)
    else:
        pass

if __name__ == '__main__':
    clean()
    prepare()
    copy_js_src()
    compress_snap()
    publish()
    input()
    
