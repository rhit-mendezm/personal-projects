import kaplay from 'https://unpkg.com/kaplay@3001/dist/kaplay.mjs';
const kplay = kaplay();

kplay.loadRoot("./");
let score = 0;

const username = sessionStorage.getItem("username") || "Guest";
let backgroundmusic = null;

const SCREEN = {
    jumpForce: 800,
    gravity: 1600,
    initialSpeed: 240,
    maxSpeed: 600,
};

function setBackGroundColor() {
    const color = kplay.rgb(135, 206, 235);
    kplay.setBackground(color);
}

function createTitleScene() {
    kplay.scene("title", () => {
        setBackGroundColor();

        kplay.add([
            kplay.text("Rosie Runner", { size: 48 }),
            kplay.pos(kplay.center().x, kplay.center().y - 40),
            kplay.anchor("center"),
        ]);

        kplay.add([
            kplay.text("Press any key to start", { size: 24 }),
            kplay.pos(kplay.center().x, kplay.center().y + 40),
            kplay.anchor("center"),
        ]);

        kplay.onKeyPress(() => kplay.go("game"));
    });
}

function createGameScene() {
    kplay.scene("game", () => {
        score = 0;
        setBackGroundColor();
        kplay.setGravity(SCREEN.gravity);

        let speed = SCREEN.initialSpeed;
        let muted = false;

        if (!backgroundmusic) {
            backgroundmusic = kplay.play("background-music", {
                loop: true,
                volume: 0.75,
            });
        }

        const soundButton = createSoundToggleButton(backgroundmusic, () => (muted = !muted));
        const rosie = createRosie();
        const scoreLabel = createScoreLabel();

        addGround();
        handleSpriteSwap(rosie);
        handleJump(rosie);
        handleObstacleCollision(rosie);
        spawnClouds();
        spawnTrees(rosie, () => {
            updateScore(scoreLabel);
            speed = Math.min(speed + 10, SCREEN.maxSpeed);
        }, () => speed);
        spawnMissiles(rosie, () => score);
    });
}

function addGround() {
    kplay.add([
        kplay.rect(kplay.width(), 48),
        kplay.pos(0, kplay.height() - 48),
        kplay.outline(4),
        kplay.area(),
        kplay.body({ isStatic: true }),
        kplay.color(19, 109, 21),
    ]);
}

function createRosie() {
    return kplay.add([
        kplay.sprite("rosie-cropped"),
        kplay.pos(80, kplay.height() - 48),
        kplay.scale(4),
        kplay.area(),
        kplay.anchor("bot"),
        kplay.body(),
        kplay.outline(1, kplay.rgb(0, 255, 0)),
        "rosie",
    ]);
}

function handleSpriteSwap(rosie) {
    kplay.onUpdate(() => {
        rosie.use(kplay.sprite(rosie.isGrounded() ? "rosie-cropped" : "rosie-2-cropped"));
    });
}

function handleJump(rosie) {
    kplay.onKeyPress("space", () => {
        if (rosie.isGrounded()) {
            rosie.jump(SCREEN.jumpForce);
            kplay.play("jumpSound", { volume: 0.6 });
        }
    });

    kplay.onKeyRelease("space", () => {
        if (rosie.vel.y < 0) {
            rosie.vel.y *= 0.4;
        }
    });
}

function handleObstacleCollision(rosie) {
    rosie.onCollide("tree", () => {
        kplay.play("kaboom", { volume: 1 });
        kplay.addKaboom(rosie.pos);
        kplay.shake();

        if (backgroundmusic) {
            backgroundmusic.stop();
            backgroundmusic = null;
        }

        kplay.go("lose");
        saveScore(score);

    });
}

function createScoreLabel() {
    return kplay.add([
        kplay.text(`User: ${username} | Score: 0`, { size: 24 }),
        kplay.pos(12, 12),
        kplay.fixed(),
        { value: 0 },
    ]);
}

function updateScore(scoreLabel) {
    scoreLabel.value += 1;
    score = scoreLabel.value; 
    scoreLabel.text = `User: ${username} | Score: ${scoreLabel.value}`;
}


function createSoundToggleButton(backgroundmusic, toggleMute) {
    let muted = false;

    const btn = kplay.add([
        kplay.sprite("musicOff"),
        kplay.pos(kplay.width() - 20, 20),
        kplay.anchor("topright"),
        kplay.fixed(),
        kplay.scale(1.5),
        kplay.area(),
        "soundButton",
    ]);

    btn.onClick(() => {
        muted = !muted;
        toggleMute(muted);
        backgroundmusic.volume = muted ? 0 : 0.75;
        btn.use(kplay.sprite(muted ? "musicOn" : "musicOff"));
    });

    return btn;
}

function spawnTrees(rosie, onScore, getSpeed) {
    function spawnTree() {
        const tree = kplay.add([
            kplay.rect(48, kplay.rand(24, 64)),
            kplay.area(),
            kplay.outline(4),
            kplay.pos(kplay.width(), kplay.height() - 48),
            kplay.anchor("botleft"),
            kplay.color(255, 180, 255),
            kplay.move(kplay.LEFT, getSpeed()),
            "tree",
            { scored: false },
        ]);

        kplay.onUpdate(() => {
            if (!tree.scored && tree.pos.x + tree.width < rosie.pos.x) {
                onScore();
                tree.scored = true;
            }
        });

        kplay.wait(kplay.rand(1.8, 2.5), spawnTree);
    }

    spawnTree();
}

function spawnClouds() {
    function spawnCloud() {
        kplay.add([
            kplay.sprite("cloud"),
            kplay.pos(kplay.width(), kplay.rand(10, kplay.height() / 2 - 50)),
            kplay.scale(kplay.rand(5, 7)),
            kplay.move(kplay.LEFT, kplay.rand(20, 40)),
            "cloud",
        ]);

        kplay.wait(kplay.rand(2, 5), spawnCloud);
    }

    spawnCloud();
}

function spawnMissiles(rosie, getScore) {
    const groundY = kplay.height() - 70;
    const maxJumpHeight = 200;

    function spawnMissile() {
        let missileSpeed = 400;
        if (score > 10) missileSpeed = 500;
        if (score > 20) missileSpeed = 700;

        const missile = kplay.add([
            kplay.sprite("missile"),
            kplay.pos(kplay.width(), kplay.rand(groundY - maxJumpHeight, groundY)),
            kplay.scale(2),
            kplay.area({ shape: new kplay.Rect(kplay.vec2(-7, -3), 14, 6) }),
            kplay.move(kplay.LEFT, missileSpeed),
            "missile",
        ]);

        missile.onUpdate(() => {
            if (missile.pos.x < -50) missile.destroy();
        });

        missile.onCollide("rosie", () => {
            kplay.play("kaboom", { volume: 1 });
            kplay.addKaboom(missile.pos);
            kplay.shake();

            if (backgroundmusic) {
                backgroundmusic.stop();
                backgroundmusic = null;
            }

            kplay.go("lose");
            saveScore(score);
        });

        kplay.wait(kplay.rand(2, 4), spawnMissile);
    }

    spawnMissile();
}

function saveScore(score) {
    const USERNAME = sessionStorage.getItem('username');
    if (!USERNAME) {
        console.warn('Username not found.');
        return;
    }
 
    const BACKEND_URL = 'http://137.112.104.54:20061/leaderboard';
   
    fetch(BACKEND_URL, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify({ username: USERNAME, score: score }),
    })
    .then(response => {
        if (!response.ok) {
            throw new Error(`HTTP error! status: ${response.status}`);
        }
        return response.json();
    })
    .catch(error => {
        console.error('Failed to save score to backend:', error);
    });
}

function createLoseScene() {
    kplay.scene("lose", () => {
        setBackGroundColor();

        kplay.add([
            kplay.text(`Game Over\nScore: ${score}`, { size: 32, align: "center" }),
            kplay.pos(kplay.center().x, kplay.center().y - 40),
            kplay.anchor("center"),
        ]);

        kplay.add([
            kplay.text("Press Enter to play again!", { size: 24 }),
            kplay.pos(kplay.center().x, kplay.center().y + 40),
            kplay.anchor("center"),
        ]);

        const leaderboardButton = kplay.add([
            kplay.rect(240, 40),
            kplay.pos(kplay.center().x, kplay.center().y + 120),
            kplay.anchor("center"),
            kplay.color(100, 100, 255),
            kplay.area(),
            kplay.outline(2),
            "leaderboardBtn",
        ]);

        kplay.add([
            kplay.text("View Leaderboard", { size: 20 }),
            kplay.pos(leaderboardButton.pos),
            kplay.anchor("center"),
        ]);

        kplay.onClick("leaderboardBtn", () => {
            window.location.href = "/leaderboard";
        });

         kplay.add([
            kplay.rect(240, 40),
            kplay.pos(leaderboardButton.pos .x, leaderboardButton.pos.y + 60),
            kplay.anchor("center"),
            kplay.color(100, 100, 255),
            kplay.area(),
            kplay.outline(2),
            kplay.fixed(),
            "homeBtn",
        ]);

        kplay.add([
            kplay.text("Go to Home", { size: 20 }),
            kplay.pos(leaderboardButton.pos .x, leaderboardButton.pos.y + 60),
            kplay.anchor("center"),
            kplay.fixed(),
        ]);

        kplay.onClick("homeBtn", () => {
            window.location.href = "/";
        });

        kplay.onKeyPress("enter", () => {
            kplay.go("game");
        });
    });
}

Promise.all([
    kplay.loadSprite("rosie-cropped", "sprites/rosie-cropped.png"),
    kplay.loadSprite("rosie-2-cropped", "sprites/rosie-2-cropped.png"),
    kplay.loadSprite("cloud", "sprites/cloud.png"),
    kplay.loadSprite("missile", "sprites/missile.png"),
    kplay.loadSprite("musicOff", "sprites/musicOff.png"),
    kplay.loadSprite("musicOn", "sprites/musicOn.png"),
    kplay.loadSound("background-music", "public/sounds/background-music.mp3"),
    kplay.loadSound("kaboom", "public/sounds/kaboom.mp3"),
    kplay.loadSound("jumpSound", "public/sounds/jumpSound.mp3"),
]).then(() => {
    createTitleScene();
    createGameScene();
    createLoseScene();
    kplay.go("title");
});