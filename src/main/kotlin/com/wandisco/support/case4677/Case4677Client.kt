package com.wandisco.support.case4677

import org.apache.hadoop.hive.conf.HiveConf
import org.apache.hadoop.hive.metastore.HiveMetaStoreClient
//import org.apache.hadoop.hive.metastore.api.MetaException
//import org.apache.hadoop.hive.metastore.api.NoSuchObjectException
import org.apache.hadoop.hive.metastore.api.Partition
//import org.apache.thrift.TException
import org.apache.thrift.transport.TTransportException

class Case4677Client(
    @Suppress("CanBeParameter") private val conf: HiveConf = HiveConf()
) {
    private val msc = HiveMetaStoreClient(conf)

    fun printGetPartitionsData(db: String?, tbl: String?, maxParts: Short) {
        println("Printing full outcome from get_partitions")
        println("============================")
        println("Database: $db - Table: $tbl - Max Partitions: $maxParts")
        println("============================")
        // listPartitions calls the needed get_partitions MSC thrift call
        val callOutput = try {
            msc.listPartitions(db, tbl, maxParts)
        } catch (e: Throwable) {
            handleException(e)
            e.cause?.filterThrowable<TTransportException> {
                println("It was also a TTransportException - Handling")
                handleException(it)
            }
            // Don't process further, allows correct interpretation of callOutput below
            return
        }

        println("Found ${callOutput.size} results")
        println("============================")
        callOutput.forEachIndexed { tblIndex, partition ->
            println("Partition index: $tblIndex")
            Partition._Fields.values().forEach { field ->
                println("Field ID: ${field.thriftFieldId}")
                println("Field name: ${field.fieldName}")
                println("Field value: ${partition.getFieldValue(field)}")
                println("====")
            }
            println("============================")
        }
        println("Completed operation")
    }

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            val conf = HiveConf().apply {
                // Apply non-default hive.metastore.uris
                args.getAsArg("metastoreUris")?.let {
                    println("Using custom hive.metastore.uris=$it")
                    set("hive.metastore.uris", it)
                }
            }
            val client = Case4677Client(conf)
            client.printGetPartitionsData(
                args.getAsArg("caseDb", CaseDefaultConstants.CASE_DB),
                args.getAsArg("caseTbl", CaseDefaultConstants.CASE_TABLE),
                CaseDefaultConstants.CASE_MAX_PARTS
            )
        }
    }
}
