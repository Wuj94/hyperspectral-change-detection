import sys
import numpy as np
import PlotterExceptions as pe
from Plotter import Plotter


class YhfyhfPlotter(Plotter):

    def __init__(self, output_filename, final_centroids_filename, figure_filename, indian_pines=False):
        super().__init__(figure_filename)
        self.__output__ = output_filename
        self.__centroids__ = final_centroids_filename
        self.__indian_pines__ = indian_pines

    def __read_centers__(self, content):
        content = content[1:len(content)-1]
        points = content.split(',')
        centers = []

        for p in points:
            components = p.split()
            centers.append([float(components[0]), float(components[1])])
        return centers

    def __read_points__(self, content):
        lines = content.split('\n')
        data = []

        cluster = []
        cluster_id = '0'
        for l in lines:
            if l[:l.find('\t')] != cluster_id:
                data.append(list(cluster))
                cluster.clear()
                cluster_id = l[:l.find('\t')]
            end = l[l.find(' ') + 1:].find(' ')
            if end == -1:
                end = len(l)
            cluster.append([float(l[l.find('\t'):l.find(' ')]), float(l[l.find(' ')+1:][: end])])
        data.append(list(cluster))
        return data

    def __read__(self):
        with open(self.__output__, 'r') as f:
            data_file_content = f.read()
        with open(self.__centroids__, 'r') as f:
            centrois_file_content = f.read()

        centers = self.__read_centers__(centrois_file_content)
        data = self.__read_points__(data_file_content[:len(data_file_content)-1])

        if len(data) != len(centers):
            raise pe.InconsistentDataset

        return np.array(data), np.array(centers)


def yhfyhf_plotter_main():
    try:
        if sys.argv[4] == 'True':
            plotter = YhfyhfPlotter(sys.argv[1], sys.argv[2], sys.argv[3], True)
        else:
            plotter = YhfyhfPlotter(sys.argv[1], sys.argv[2], sys.argv[3], False)
        plotter.plot()
    except pe.PlotGeneratorError as e:
        print(e)


yhfyhf_plotter_main()

