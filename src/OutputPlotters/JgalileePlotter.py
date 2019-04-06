import sys
import numpy as np
import PlotterExceptions as pe
from Plotter import Plotter


class JgalileePlotter(Plotter):

    def __init__(self, output_filename, data_filename, points_per_cluster, figure_filename, indian_pines=False):
        """set points_per_cluster != 0 to plot an artificial data set.
        set indian_pines = True to plot Indian Pines data set.
        points_per_cluster != 0 if and only if indian_pines=False."""
        if points_per_cluster == 0 and indian_pines is False:
            raise Exception("Cannot construct an object with indian_pines=False and points_per_cluster=0")
        super().__init__(figure_filename)
        self.__output__ = output_filename
        self.__data__ = data_filename
        self.__points_per_cluster__ = points_per_cluster
        self.__indian_pines__ = indian_pines
        self.__ip_spectral_centers__ = None

    def __read_artificial_centers__(self, content):
        centers = []

        lines = content.split('\n')
        for l in lines:
            components = l[2:].split()
            centers.append([float(components[0]), float(components[1])])

        return centers

    def __read_indian_pines_centers__(self, content):
        centers = []
        self.__ip_spectral_centers__ = []

        lines = content.split('\n')
        for l in lines:
            components = l[2:].split()
            centers.append([float(components[0]), float(components[1])])
            self.__ip_spectral_centers__.append([float(components[i]) for i in range(2, 202)])

        return centers

    def __read_artificial_points__(self, content):
        data = []

        lines = content.split('\n')
        cluster = []
        i = 0
        for l in lines:
            if i == self.__points_per_cluster__:
                data.append(list(cluster))
                cluster.clear()
                i = 0
            components = l.split()
            cluster.append([float(components[0]), float(components[1])])
            i = i + 1
        data.append(list(cluster))

        return data

    def __read_indian_pines_points__(self, content):
        data = []
        for i in range(len(self.__ip_spectral_centers__)):
            data.append([])

        lines = content.split('\n')
        for l in lines:
            components = l.split()
            spectral_part = [float(components[i]) for i in range(2, 202)]
            pixel_attr = [float(components[i]) for i in range(0, 2)]
            belonging_cluster = self.__nearest_center__(spectral_part)
            data[belonging_cluster].append(pixel_attr)

        return np.array(data)

    def __nearest_center__(self, spectral_part):
        dists = []
        for c in self.__ip_spectral_centers__:
            dists.append(self.__distance__(spectral_part, c))
        return dists.index(min(dists))

    def __distance__(self, point1, point2):
        """squared euclidean distance"""
        sum = 0
        for p1, p2 in zip(point1, point2):
            sum += (p1 - p2)**2
        return sum

    def __read_points__(self, content):
        if self.__indian_pines__:
            return self.__read_indian_pines_points__(content)
        else:
            return self.__read_artificial_points__(content)

    def __read_centers__(self, content):
        if self.__indian_pines__:
            return self.__read_indian_pines_centers__(content)
        else:
            return self.__read_artificial_centers__(content)

    def __read__(self):
        with open(self.__data__, 'r') as f:
            data_file_content = f.read()
        with open(self.__output__, 'r') as f:
            centrois_file_content = f.read()
        centrois_file_content = centrois_file_content[:len(centrois_file_content)-1]

        centers = self.__read_centers__(centrois_file_content)
        data = self.__read_points__(data_file_content[:len(data_file_content)-1])

        if len(data) != len(centers):
            raise pe.InconsistentDataset()

        return np.array(data), np.array(centers)

def print_help():
    print("Program USAGE: [CENTERS] [DATA] [PTS] [OUTPUT] [ARTIFICIAL]")
    print("\t[CENTERS]: clustering resulting centers")
    print("\t[DATA]: dataset")
    print("\t[PTS]: points per cluster (ARTIFICIAL have to be true if PTS is not equal 0)")
    print("\t[OUTPUT]: output filename")
    print("\t[ARTIFICIAL]: True if the data set is artificial (implies PTS not equal 0), False otherwise")

def jgalilee_plotter_main():
    try:
        #output_filename, data_filename, points_per_cluster, figure_filename, indian_pines=False
        if sys.argv[1] == '-h' or sys.argv[1] == '--help' or sys.argv[1] is None:
            print_help()
        if sys.argv[5] == 'True':
            plotter = JgalileePlotter(sys.argv[1], sys.argv[2], int(sys.argv[3]), sys.argv[4], True)
            plotter.plot()
        elif sys.argv[5] == 'False':
            plotter = JgalileePlotter(sys.argv[1], sys.argv[2], int(sys.argv[3]), sys.argv[4], False)
            plotter.plot()
        else:
            print_help()

    except pe.PlotGeneratorError as e:
        print(e)
    except Exception as e:
        print(e)


jgalilee_plotter_main()
