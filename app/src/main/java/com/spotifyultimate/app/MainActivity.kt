package com.spotifyultimate.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val SpotifyGreen = Color(0xFF1ED760)
private val Night = Color(0xFF101010)
private val Panel = Color(0xFF1D1D1D)
private val Muted = Color(0xFFB3B3B3)
private val AppleAccent = Color(0xFFE7E7E7)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { UltimateApp() }
    }
}

private enum class Tab(val label: String) {
    HOME("Home"), SEARCH("Search"), LIBRARY("Your Library"), EDITIONS("My Editions")
}

private data class Edition(val title: String, val subtitle: String, val tracks: Int)

@Composable
private fun UltimateApp() {
    var selectedTab by remember { mutableStateOf(Tab.HOME) }
    var appleStyle by remember { mutableStateOf(false) }
    var showCreate by remember { mutableStateOf(false) }
    var search by remember { mutableStateOf("") }
    var editions by remember {
        mutableStateOf(
            listOf(
                Edition("DONDA — Personal Edition", "Kanye West · Edição personalizada", 27),
                Edition("Blonde — Late Night Cut", "Frank Ocean · Edição personalizada", 12)
            )
        )
    }

    val accent = if (appleStyle) AppleAccent else SpotifyGreen
    val bg = if (appleStyle) Color(0xFF080808) else Night

    MaterialTheme(
        colorScheme = darkColorScheme(
            background = bg,
            surface = Panel,
            primary = accent,
            onPrimary = Color.Black,
            onBackground = Color.White,
            onSurface = Color.White
        )
    ) {
        Scaffold(
            containerColor = bg,
            bottomBar = {
                NavigationBar(containerColor = Color(0xFF080808), contentColor = Muted) {
                    val tabs = listOf(
                        Tab.HOME to Icons.Default.Home,
                        Tab.SEARCH to Icons.Default.Search,
                        Tab.LIBRARY to Icons.Default.LibraryMusic,
                        Tab.EDITIONS to Icons.Default.Album
                    )
                    tabs.forEach { (tab, icon) ->
                        NavigationBarItem(
                            selected = selectedTab == tab,
                            onClick = { selectedTab = tab },
                            icon = { Icon(icon, contentDescription = tab.label) },
                            label = { Text(tab.label, fontSize = 9.sp, maxLines = 1) },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = accent,
                                selectedTextColor = Color.White,
                                indicatorColor = Color(0xFF292929),
                                unselectedIconColor = Muted,
                                unselectedTextColor = Muted
                            )
                        )
                    }
                }
            }
        ) { padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .background(bg)
            ) {
                TopHeader(
                    appleStyle = appleStyle,
                    onToggle = { appleStyle = !appleStyle },
                    accent = accent
                )
                when (selectedTab) {
                    Tab.HOME -> HomeScreen(accent)
                    Tab.SEARCH -> SearchScreen(search, { search = it })
                    Tab.LIBRARY -> LibraryScreen()
                    Tab.EDITIONS -> EditionsScreen(editions, { showCreate = true }, accent)
                }
                Spacer(Modifier.height(8.dp))
                MiniPlayer(accent)
            }
        }

        if (showCreate) {
            var title by remember { mutableStateOf("") }
            AlertDialog(
                onDismissRequest = { showCreate = false },
                title = { Text("Criar edição") },
                text = {
                    OutlinedTextField(
                        value = title,
                        onValueChange = { title = it },
                        label = { Text("Nome da edição") },
                        singleLine = true
                    )
                },
                confirmButton = {
                    TextButton(onClick = {
                        if (title.isNotBlank()) {
                            editions = listOf(Edition(title.trim(), "Edição personalizada", 0)) + editions
                        }
                        showCreate = false
                    }) { Text("Criar") }
                },
                dismissButton = {
                    TextButton(onClick = { showCreate = false }) { Text("Cancelar") }
                }
            )
        }
    }
}

@Composable
private fun TopHeader(appleStyle: Boolean, onToggle: () -> Unit, accent: Color) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            Text("SPOTIFY ULTIMATE", color = accent, fontSize = 11.sp, fontWeight = FontWeight.Bold, letterSpacing = 1.5.sp)
            Text(
                when (appleStyle) {
                    true -> "Apple-inspired"
                    false -> "Sua música, do seu jeito"
                },
                color = Muted,
                fontSize = 12.sp
            )
        }
        Surface(
            color = Panel,
            shape = CircleShape,
            modifier = Modifier.clickable(onClick = onToggle)
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 9.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(Icons.Default.Palette, null, tint = accent, modifier = Modifier.size(16.dp))
                Spacer(Modifier.width(6.dp))
                Text(if (appleStyle) "Apple" else "Spotify", color = Color.White, fontSize = 11.sp)
            }
        }
    }
}

@Composable
private fun HomeScreen(accent: Color) {
    LazyColumn(
        modifier = Modifier.fillMaxWidth().weight(1f),
        contentPadding = PaddingValues(horizontal = 18.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(18.dp)
    ) {
        item {
            Text("Boa noite", color = Color.White, fontSize = 28.sp, fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(12.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                QuickTile("Suas músicas", "Biblioteca")
                QuickTile("My Editions", "Coleções")
            }
        }
        item {
            Text("Feito para você", color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(10.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                AlbumTile("Currents", "Tame Impala", Color(0xFFB86B35))
                AlbumTile("Blonde", "Frank Ocean", Color(0xFFB9A17C))
                AlbumTile("DONDA", "Kanye West", Color(0xFF333333))
            }
        }
        item {
            Text("Continue explorando", color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold)
            Text("Conecte o Spotify para carregar sua música e biblioteca reais.", color = Muted, fontSize = 13.sp)
        }
    }
}

@Composable
private fun SearchScreen(search: String, onSearch: (String) -> Unit) {
    Column(Modifier.fillMaxSize().padding(horizontal = 18.dp).weight(1f)) {
        Text("Buscar", color = Color.White, fontSize = 28.sp, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(14.dp))
        OutlinedTextField(
            value = search,
            onValueChange = onSearch,
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("O que você quer ouvir?") },
            leadingIcon = { Icon(Icons.Default.Search, null) },
            singleLine = true,
            shape = RoundedCornerShape(14.dp)
        )
        Spacer(Modifier.height(18.dp))
        Text("Sua busca aparecerá aqui quando conectarmos sua conta.", color = Muted, fontSize = 13.sp)
    }
}

@Composable
private fun LibraryScreen() {
    Column(Modifier.fillMaxSize().padding(horizontal = 18.dp).weight(1f)) {
        Text("Sua Biblioteca", color = Color.White, fontSize = 28.sp, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(12.dp))
        Text("Álbuns, artistas e playlists da sua conta serão carregados após a integração autorizada.", color = Muted, fontSize = 14.sp)
    }
}

@Composable
private fun EditionsScreen(editions: List<Edition>, onCreate: () -> Unit, accent: Color) {
    Column(Modifier.fillMaxSize().weight(1f)) {
        Row(
            Modifier.fillMaxWidth().padding(horizontal = 18.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text("My Editions", color = Color.White, fontSize = 27.sp, fontWeight = FontWeight.Bold)
                Text("Suas versões personalizadas", color = Muted, fontSize = 12.sp)
            }
            FilledIconButton(onClick = onCreate, colors = IconButtonDefaults.filledIconButtonColors(containerColor = accent)) {
                Icon(Icons.Default.Add, contentDescription = "Criar edição", tint = Color.Black)
            }
        }
        LazyColumn(
            contentPadding = PaddingValues(18.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(editions) { edition ->
                Card(colors = CardDefaults.cardColors(containerColor = Panel), shape = RoundedCornerShape(14.dp)) {
                    Row(Modifier.fillMaxWidth().padding(14.dp), verticalAlignment = Alignment.CenterVertically) {
                        Surface(color = Color(0xFF343434), shape = RoundedCornerShape(10.dp), modifier = Modifier.size(54.dp)) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(Icons.Default.Album, null, tint = accent, modifier = Modifier.size(27.dp))
                            }
                        }
                        Spacer(Modifier.width(12.dp))
                        Column(Modifier.weight(1f)) {
                            Text(edition.title, color = Color.White, fontWeight = FontWeight.SemiBold, maxLines = 1, overflow = TextOverflow.Ellipsis)
                            Text(edition.subtitle, color = Muted, fontSize = 11.sp)
                            Text("${edition.tracks} faixas", color = Muted, fontSize = 10.sp)
                        }
                        Icon(Icons.Default.ChevronRight, null, tint = Muted)
                    }
                }
            }
        }
    }
}

@Composable
private fun QuickTile(title: String, subtitle: String) {
    Card(
        modifier = Modifier.weight(1f).height(64.dp),
        colors = CardDefaults.cardColors(containerColor = Panel),
        shape = RoundedCornerShape(10.dp)
    ) {
        Column(Modifier.padding(10.dp), verticalArrangement = Arrangement.Center) {
            Text(title, color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.SemiBold, maxLines = 1)
            Text(subtitle, color = Muted, fontSize = 10.sp)
        }
    }
}

@Composable
private fun AlbumTile(title: String, artist: String, color: Color) {
    Column(Modifier.width(112.dp)) {
        Surface(color = color, shape = RoundedCornerShape(8.dp), modifier = Modifier.size(112.dp)) {
            Box(contentAlignment = Alignment.Center) {
                Icon(Icons.Default.MusicNote, null, tint = Color.White.copy(alpha = 0.8f), modifier = Modifier.size(38.dp))
            }
        }
        Spacer(Modifier.height(7.dp))
        Text(title, color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.SemiBold, maxLines = 1)
        Text(artist, color = Muted, fontSize = 10.sp, maxLines = 1)
    }
}

@Composable
private fun MiniPlayer(accent: Color) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 10.dp).background(Color(0xFF282828), RoundedCornerShape(10.dp)).padding(10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Surface(color = Color(0xFF4A4A4A), shape = RoundedCornerShape(6.dp), modifier = Modifier.size(38.dp)) {
            Box(contentAlignment = Alignment.Center) {
                Icon(Icons.Default.MusicNote, null, tint = Color.White)
            }
        }
        Spacer(Modifier.width(10.dp))
        Column(Modifier.weight(1f)) {
            Text("Spotify desconectado", color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
            Text("Conectaremos o player na próxima etapa", color = Muted, fontSize = 9.sp, maxLines = 1)
        }
        Icon(Icons.Default.PlayArrow, null, tint = accent, modifier = Modifier.size(27.dp))
    }
}
