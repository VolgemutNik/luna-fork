package api.predef

import com.google.common.base.Stopwatch
import com.google.common.collect.HashBasedTable
import com.google.common.collect.ImmutableTable
import com.google.common.collect.Table
import io.luna.game.model.def.EquipmentDefinition
import io.luna.game.model.def.ItemDefinition
import io.luna.game.model.def.NpcDefinition
import io.luna.game.model.def.ObjectDefinition
import io.luna.util.StringUtils
import java.time.Duration
import java.util.concurrent.ThreadLocalRandom
import java.util.concurrent.TimeUnit

/**
 * Returns the current time in [TimeUnit.MILLISECONDS], using [System.nanoTime] for better precision.
 */
fun currentTimeMs() = TimeUnit.MILLISECONDS.convert(System.nanoTime(), TimeUnit.NANOSECONDS)

/**
 * Times how long it takes for the code within [block] to complete.
 */
fun time(block: () -> Unit): Duration {
    val stopwatch = Stopwatch.createStarted()
    block()
    return stopwatch.elapsed()
}

/**
 * A shortcut to the lazy delegate property. Initializations are **not** thread safe!
 */
fun <T> lazyVal(initializer: () -> T): Lazy<T> = lazy(LazyThreadSafetyMode.NONE, initializer)

/**
 * Computes and returns the name for [id].
 */
fun itemName(id: Int): String = ItemDefinition.ALL[id].map { it.name }
        .orElseThrow { NoSuchElementException("Name not found for item <$id>") }

/**
 * Computes and returns the [ItemDefinition] for [id].
 */
fun itemDef(id: Int): ItemDefinition = ItemDefinition.ALL.get(id)
        .orElseThrow { NoSuchElementException("Definition not found for item <$id>") }

/**
 * Computes and returns the [EquipmentDefinition] for [id].
 */
fun equipDef(id: Int): EquipmentDefinition = EquipmentDefinition.ALL.get(id)
        .orElseThrow { NoSuchElementException("Definition not found for item <$id>") }

/**
 * Computes and returns the [NpcDefinition] for [id].
 */
fun npcDef(id: Int): NpcDefinition = NpcDefinition.ALL.get(id)
        .orElseThrow { NoSuchElementException("Definition not found for npc <$id>") }

/**
 * Computes and returns the [ObjectDefinition] for [id].
 */
fun objectDef(id: Int): ObjectDefinition = ObjectDefinition.ALL.get(id)
        .orElseThrow { NoSuchElementException("Definition not found for object <$id>") }

/**
 * Forwards to [StringUtils.addArticle], returns an empty string if [thing] is null.
 */
fun addArticle(thing: Any?): String =
        when (thing) {
            null -> ""
            else -> StringUtils.addArticle(thing)
        }

/**
 * Returns the current [ThreadLocalRandom].
 */
fun rand() = ThreadLocalRandom.current()!!

/**
 * Generates a random integer from [lower] to [upperInclusive] inclusive.
 */
fun rand(lower: Int, upperInclusive: Int): Int = rand().nextInt((upperInclusive - lower) + 1) + lower

/**
 * Generates a random integer from 0 to [upperInclusive] inclusive.
 */
fun rand(upperInclusive: Int): Int = rand().nextInt(upperInclusive + 1)

/**
 * Generates a random integer within [range].
 */
fun rand(range: IntRange) = rand(range.first, range.last)

/**
 * Creates an empty mutable table of [entries].
 */
fun <R, C, V> emptyTable(size: Int = 16): Table<R, C, V> = HashBasedTable.create(size, size)

/**
 * Creates an immutable table of [entries]. The syntax for creating entries is (row [to] column [to] value).
 */
fun <R, C, V> immutableTableOf(vararg entries: TableEntry<R, C, V>): Table<R, C, V> =
        when (entries.size) {
            0 -> ImmutableTable.of()
            1 -> {
                val entry = entries[0]
                val key = entry.first
                ImmutableTable.of(key.first, key.second, entry.second)
            }

            else -> {
                val table = ImmutableTable.builder<R, C, V>()
                for (entry in entries) {
                    val key = entry.first
                    table.put(key.first, key.second, entry.second)
                }
                table.build()
            }
        }