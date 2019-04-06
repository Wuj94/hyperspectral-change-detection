class PlotGeneratorError(Exception):
    pass


class DifferentSpaceError(PlotGeneratorError):
    """Exception raised when two vectors under comparison lie in two different spaces"""

    def __init__(self, message):
        self.message = message
        if message is None:
            self.message = 'Vectors lie in two different spaces'


class InconsistentDataset(PlotGeneratorError):
    """Exception raised when the data set does not have the same number of cluster and centroids"""

    def __init__(self, message):
        self.message = message
        if message is None:
            self.message = 'Inconsistent data set'
