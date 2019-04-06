import sys
import pathlib
import PlotterExceptions as pe
import numpy as np
from Plotter import Plotter


class BrizziPlotter(Plotter):

    def __init__(self, filenames, figure_filename):
        super().__init__(figure_filename)
        self.filenames = filenames

    def __parse_center__(self, line):
        center = []

        center_points = line.split()
        for i in range(2):
            center.append(float(center_points[i]))
        return center

    def __parse_points__(self, data_lines):
        data_points = []
        for dl in data_lines:
            components = dl.split()
            if len(components) != 0:
                data_points.append([float(components[0]), float(components[1])])
        return np.array(data_points)

    def __read__(self):
        data = []
        centers = []

        files = []  # a list of strings, each containing an entire file
        for f in self.filenames:
            with open(f, 'r') as file:
                if file.readline() != '':
                    file.seek(0)
                    files.append(file.read())

        for f in files:
            data_lines = []
            lines = f.split('\n')
            for l in lines[:len(lines) - 1]:  # remove trailing \n
                if l != '':
                    if not l.startswith('\t'):
                        if len(data_lines) != 0:
                            data.append(self.__parse_points__(data_lines))
                        centers.append(self.__parse_center__(l))
                        data_lines.clear()
                    else:
                        data_lines.append(l)
            data.append(self.__parse_points__(data_lines))

        if len(data) != len(centers):
            raise pe.InconsistentDataset

        return data, centers


def brizzi_plotter_main():
    path = pathlib.Path(sys.argv[1])

    filenames = []
    for child in path.iterdir():
        if child.is_file():
            if child.name.find('part-r-') != -1 and child.name.find('.crc') == -1:
                filenames.append(child)
    try:
        plotter = BrizziPlotter(filenames, sys.argv[2])
        plotter.plot()
    except pe.PlotGeneratorError as e:
        print(e)


brizzi_plotter_main()
