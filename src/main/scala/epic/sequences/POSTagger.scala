package epic.sequences

import breeze.optimize.FirstOrderMinimizer.OptParams
import epic.trees.{AnnotatedLabel, ProcessedTreebank}
import breeze.config.CommandLineParser
import epic.framework.ModelObjective
import breeze.optimize.CachedBatchDiffFunction

/**
 *
 * @author dlwh
 */
object POSTagger {
  case class Params(opt: OptParams, treebank: ProcessedTreebank)

  def main(args: Array[String]) {
    val params = CommandLineParser.readIn[Params](args)
    import params._
    val train = treebank.trainTrees.map(_.toTaggedSequence)
    val test = treebank.devTrees.map(_.toTaggedSequence)

    val crf = CRF.buildSimple(train, AnnotatedLabel("TOP"), opt = opt)
    val stats = TaggedSequenceEval.eval(crf, test)
    println("Final Stats: " + stats)

  }

}