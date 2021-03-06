package com.gbeatty.arxivexplorer.arxivdata;

import com.gbeatty.arxivexplorer.models.Category;

public class Categories {

    public static final Category[] CATEGORIES = {

            new Category("Astrophysics", "astro-ph",
                    new Category("All", "astro-ph"),
                    new Category("Astrophysics of Galaxies", "astro-ph.GA"),
                    new Category("Cosmology and Nongalactic Astrophysics", "astro-ph.CO"),
                    new Category("Earth and Planetary Astrophysics", "astro-ph.EP"),
                    new Category("High Energy Astrophysical Phenomena", "astro-ph.HE"),
                    new Category("Instrumentation and Methods for Astrophysics", "astro-ph.IM"),
                    new Category("Solar and Stellar Astrophysics", "astro-ph.SR")
            ),
            new Category("Computer Science", "CoRR",
                    new Category("All", "CoRR"),
                    new Category("Artificial Intelligence", "cs.AI"),
                    new Category("Computation and Language", "cs.CL"),
                    new Category("Computational Complexity", "cs.CC"),
                    new Category("Computational Engineering, Finance, and Science", "cs.CE"),
                    new Category("Computational Geometry", "cs.CG"),
                    new Category("Computer Science and Game Theory", "cs.GT"),
                    new Category("Computer Vision and Pattern Recognition", "cs.CV"),
                    new Category("Computers and Society", "cs.CY"),
                    new Category("Cryptography and Security", "cs.CR"),
                    new Category("Data Structures and Algorithms", "cs.DS"),
                    new Category("Databases", "cs.DB"),
                    new Category("Digital Libraries", "cs.DL"),
                    new Category("Discrete Mathematics", "cs.DM"),
                    new Category("Distributed, Parallel, and Cluster Computing", "cs.DC"),
                    new Category("Emerging Technologies", "cs.ET"),
                    new Category("Formal Languages and Automata Theory", "cs.FL"),
                    new Category("General Literature", "cs.GL"),
                    new Category("Graphics", "cs.GR"),
                    new Category("Hardware Architecture", "cs.AR"),
                    new Category("Human-Computer Interaction", "cs.HC"),
                    new Category("Information Retrieval", "cs.IR"),
                    new Category("Information Theory", "cs.IT"),
                    new Category("Machine Learning", "cs.LG"),
                    new Category("Logic in Computer Science", "cs.LO"),
                    new Category("Mathematical Software", "cs.MS"),
                    new Category("Multiagent Systems", "cs.MA"),
                    new Category("Multimedia", "cs.MM"),
                    new Category("Networking and Internet Architecture", "cs.NI"),
                    new Category("Neural and Evolutionary Computing", "cs.NE"),
                    new Category("Numerical Analysis", "cs.NA"),
                    new Category("Operating Systems", "cs.OS"),
                    new Category("Other", "cs.OH"),
                    new Category("Performance", "cs.PF"),
                    new Category("Programming Languages", "cs.PL"),
                    new Category("Robotics", "cs.RO"),
                    new Category("Social and Information Networks", "cs.SI"),
                    new Category("Software Engineering", "cs.SE"),
                    new Category("Sound", "cs.SD"),
                    new Category("Symbolic", "cs.SC"),
                    new Category("Systems and Control", "cs.SY")
            ),
            new Category("Condensed Matter", "cond-mat",
                    new Category("All", "cond-mat"),
                    new Category("Disordered Systems and Neural Networks", "cond-mat.dis-nn"),
                    new Category("Materials Science", "cond-mat.mtrl-sci"),
                    new Category("Mesoscale and Nanoscale Physics", "cond-mat.mes-hall"),
                    new Category("Other Condensed Matter", "cond-mat..other"),
                    new Category("Quantum Gases", "cond-mat.quant-gas"),
                    new Category("Soft Condensed Matter", "cond-mat.soft"),
                    new Category("Statistical Mechanics", "cond-mat.stat-mech"),
                    new Category("Strongly Correlated Electrons", "cond-mat.str-el"),
                    new Category("Superconductivity", "cond-mat.supr-con")
            ),
            new Category("Economics", "econ",
                    new Category("Econometrics", "eess.AS")
            ),
            new Category("Electrical Engineering", "eess",
                    new Category("All", "eess"),
                    new Category("Audio and Speech Processing", "eess.AS"),
                    new Category("Image and Video Processing", "eess.IV"),
                    new Category("Signal Processing", "eess.SP")
            ),
            new Category("General Relativity and Quantum Cosmology", "gr-qc",
                    (Category[]) null
            ),
            new Category("High Energy Physics - Experiment", "hep-ex",
                    (Category[]) null
            ),
            new Category("High Energy Physics - Lattice", "hep-lat",
                    (Category[]) null
            ),
            new Category("High Energy Physics - Phenomenology", "hep-ph",
                    (Category[]) null
            ),
            new Category("High Energy Physics - Theory", "hep-th",
                    (Category[]) null
            ),
            new Category("Mathematics", "math",
                    new Category("All", "math"),
                    new Category("Algebraic Geometry", "math.AG"),
                    new Category("Algebraic Topology", "math.AT"),
                    new Category("Analysis of PDEs", "math.AP"),
                    new Category("Category Theory", "math.CT"),
                    new Category("Classical Analysis and ODEs", "math.CA"),
                    new Category("Combinatorics", "math.CO"),
                    new Category("Commutative Algebra", "math.AC"),
                    new Category("Complex Variables", "math.CV"),
                    new Category("Differential Geometry", "math.DG"),
                    new Category("Dynamical Systems", "math.DS"),
                    new Category("Functional Analysis", "math.FA"),
                    new Category("General Mathematics", "math.GM"),
                    new Category("General Topology", "math.GN"),
                    new Category("Geometric Topology", "math.GT"),
                    new Category("Group Theory", "math.GR"),
                    new Category("History and Overview", "math.HO"),
                    new Category("Information Theory", "math.IT"),
                    new Category("K-Theory and Homology", "math.KT"),
                    new Category("Logic", "math.LO"),
                    new Category("Mathematical Physics", "math.MP"),
                    new Category("Metric Geometry", "math.MG"),
                    new Category("Number Theory", "math.NT"),
                    new Category("Numerical Analysis", "math.NA"),
                    new Category("Operator Algebras", "math.OA"),
                    new Category("Optimization and Control", "math.OC"),
                    new Category("Probability", "math.PR"),
                    new Category("Quantum Algebra", "math.QA"),
                    new Category("Representation Theory", "math.RT"),
                    new Category("Rings and Algebras", "math.RA"),
                    new Category("Spectral Theory", "math.SP"),
                    new Category("Statistics Theory", "math.ST"),
                    new Category("Symplectic Geometry", "math.SG")
            ),
            new Category("Mathematical Physics", "math-ph",
                    (Category[]) null
            ),
            new Category("Nonlinear Sciences", "nlin",
                    new Category("All", "nlin"),
                    new Category("Adaptation and Self-Organizing Systems", "nlin.AO"),
                    new Category("Cellular Automata and Lattice Gases", "nlin.CG"),
                    new Category("Chaotic Dynamics", "nlin.CD"),
                    new Category("Exactly Solvable and Integrable Systems", "nlin.SI"),
                    new Category("Pattern Formation and Solitons", "nlin.PS")

            ),
            new Category("Nuclear Experiment", "nucl-ex",
                    (Category[]) null
            ),
            new Category("Nuclear Theory", "nucl-th",
                    (Category[]) null
            ),
            new Category("Physics", "physics",
                    new Category("All", "physics"),
                    new Category("Accelerator Physics", "physics.acc-ph"),
                    new Category("Applied Physics", "physics.app-ph"),
                    new Category("Atmospheric and Oceanic Physics", "physics.ao-ph"),
                    new Category("Atomic Physics", "physics.atom-ph"),
                    new Category("Atomic and Molecular Clusters", "physics.atm-clus"),
                    new Category("Biological Physics", "physics.bio-ph"),
                    new Category("Chemical Physics", "physics.chem-ph"),
                    new Category("Classical Physics", "physics.class-ph"),
                    new Category("Computational Physics", "physics.comp-ph"),
                    new Category("Data Analysis, Statistics and Probability", "physics.data-an"),
                    new Category("Fluid Dynamics", "physics.flu-dyn"),
                    new Category("General Physics", "physics.gen-ph"),
                    new Category("Geophysics", "physics.geo-ph"),
                    new Category("History and Philosophy of Physics", "physics.hist-ph"),
                    new Category("Instrumentation and Detectors", "physics.ins-det"),
                    new Category("Medical Physics", "physics.med-ph"),
                    new Category("Optics", "physics.optics"),
                    new Category("Physics Education", "physics.ed-ph"),
                    new Category("Physics and Society", "physics.soc-ph"),
                    new Category("Plasma Physics", "physics.plasm-ph"),
                    new Category("Popular Physics", "physics.pop-ph"),
                    new Category("Space Physics", "physics.space-ph")
            ),

            new Category("Quantitative Biology", "q-bio",
                    new Category("All", "q-bio"),
                    new Category("Biomolecules", "q-bio.BM"),
                    new Category("Cell Behavior", "q-bio.CB"),
                    new Category("Genomics", "q-bio.GN"),
                    new Category("Molecular Networks", "q-bio.MN"),
                    new Category("Neurons and Cognition", "q-bio.NC"),
                    new Category("Other Quantitative Biology", "q-bio.OT"),
                    new Category("Populations and Evolution", "q-bio.PE"),
                    new Category("Quantitative Methods", "q-bio.QM"),
                    new Category("Subcellular Processes", "q-bio.SC"),
                    new Category("Tissues and Organs", "q-bio.TO")
            ),
            new Category("Quantitative Finance", "q-fin",
                    new Category("All", "q-fin."),
                    new Category("Computational Finance", "q-fin.CP"),
                    new Category("Economics", "q-fin.EC"),
                    new Category("General Finance", "q-fin.GN"),
                    new Category("Mathematical Finance", "q-fin.MF"),
                    new Category("Portfolio Management", "q-fin.PM"),
                    new Category("Pricing of Securities", "q-fin.PR"),
                    new Category("Risk Management", "q-fin.RM"),
                    new Category("Statistical Finance", "q-fin.ST"),
                    new Category("Trading and Market Microstructure", "q-fin.TR")
            ),
            new Category("Quantum Physics", "quant-ph",
                    (Category[]) null
            ),
            new Category("Statistics", "stat",
                    new Category("All", "stat"),
                    new Category("Applications", "stat.AP"),
                    new Category("Computation", "stat.CO"),
                    new Category("Machine Learning", "stat.ML"),
                    new Category("Methodology", "stat.ME"),
                    new Category("Other Statistics", "stat.OT"),
                    new Category("Statistics Theory", "stat.TH")
            ),
    };
}
